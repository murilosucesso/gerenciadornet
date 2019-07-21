package br.com.gerenciadornet.session;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.BeanUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.framework.EntityHome;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import br.com.gerenciadornet.entity.Categoria;
import br.com.gerenciadornet.entity.ConteudoImagem;
import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.ImagemProduto;
import br.com.gerenciadornet.entity.LoteProduto;
import br.com.gerenciadornet.entity.Marca;
import br.com.gerenciadornet.entity.Produto;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.LogUtil;

@SuppressWarnings("serial")
@Name("produtoHome")
public class ProdutoHome extends EntityHome<Produto> {

	@In(create = true)
	CategoriaHome categoriaHome;

	@In(create = true)
	MarcaHome marcaHome;

	@In
	EntityManager entityManager;

	@In(create = true)
	Usuario user;

	@In(create = true)
	ProdutoList produtoList;

	@In(create = true)
	ImagemProdutoHome imagemProdutoHome;

	List<ImagemProduto> imagensProduto = new ArrayList<ImagemProduto>();
	
	@Out(required=false, value="imagemToShow", scope=ScopeType.EVENT)
	ImagemProduto imagemToShow = null;

	boolean wired = false;

	public void setProdutoCodProduto(Integer id) {
		setId(id);
	}

	public Integer getProdutoCodProduto() {
		return (Integer) getId();
	}

	@Override
	protected Produto createInstance() {
		Produto produto = new Produto();
		return produto;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		prepareImagensToUpdate();
		Categoria categoria = categoriaHome.getDefinedInstance();
		if (categoria != null) {
			getInstance().setCategoria(categoria);
		}
		Marca marca = marcaHome.getDefinedInstance();
		if (marca != null) {
			getInstance().setMarca(marca);
		}
		wired = true;
	}

	public boolean isWired() {
		return true;
	}

	public Produto getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<LoteProduto> getLoteProdutos() {
		return getInstance() == null ? null : new ArrayList<LoteProduto>(
				getInstance().getLoteProdutos());
	}

	@Override
	public String remove() {

		Produto produto = getDefinedInstance();

		String log = "";

		if (produto.getLoteProdutos().size() == 0) {
			super.remove();
			addFacesMessage("O produto "
					+ produto.getNomeProduto().toUpperCase()
					+ " foi excluido com sucesso.", "");
			log = LogUtil.logHistoricoDelete("Produto",
					produto.getCodProduto(), produto.getNomeProduto()
							.toUpperCase());

		} else {
			produto.setInExclusao(true);
			update();
			log = LogUtil.logHistoricoDesativada("Produto", produto
					.getCodProduto(), produto.getNomeProduto().toUpperCase());
			addFacesMessage("O produto"
					+ produto.getNomeProduto().toUpperCase()
					+ " foi desabilitado com sucesso.", "");
		}

		Historico historico = new Historico(user, log, new Date(
				System.currentTimeMillis()));
		entityManager.persist(historico);

		return "removed";
	}

	/*
	 * Adiciona uma imagem na lista de Exibição para a View e na lista de
	 * Imagens para o Objeto. Tal ação faz-se necessária devido a falha de carga
	 * de bytes[] para a apresentação 'xhtml'
	 * 
	 * @param event
	 */
	public void adicionarImagemListener(UploadEvent event) {
		if (imagensProduto.size() < 3) {
			UploadItem item = event.getUploadItem();
			ImagemProduto imagem = new ImagemProduto();
			imagem.setExtensao(item.getContentType());
			imagem.setNome(item.getFileName());
			imagem.setTamanho((long) item.getFileSize());
			imagem.setProduto(getInstance());
			ImagemProduto clone = new ImagemProduto();
			try {
				BeanUtils.copyProperties(clone, imagem);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			ConteudoImagem conteudoImagem = new ConteudoImagem();
			conteudoImagem.setImagemProduto(clone);
			clone.setConteudoImagem(conteudoImagem);
			clone.getConteudoImagem().setBytes(getData(item.getFile()));
			if (isManaged()) {
				imagemProdutoHome.setInstance(clone);
				imagemProdutoHome.persist();
			}
			imagensProduto.add(imagem);
			getInstance().getImagensProduto().add(clone);
		}
	}

	/*
	 * Atualiza a lista de Imagens ja cadastradas para o Produto que esta sendo
	 * atualizado
	 */
	private void prepareImagensToUpdate() {
		if (isManaged()) {
			for (ImagemProduto imagemProduto : getInstance()
					.getImagensProduto()) {
				ImagemProduto toView = new ImagemProduto();
				try {
					BeanUtils.copyProperties(toView, imagemProduto);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				toView.setConteudoImagem(null);
				if (!imagensProduto.contains(toView))
					imagensProduto.add(toView);
			}
		}
	}

	/*
	 * Remove a imagem da lista de Imagens a ser apresentada em tela e do objeto
	 * Produto
	 * 
	 * @param imagemProduto
	 */
	@Transactional
	public void removerImagem(ImagemProduto imagemProduto) {
		if (isManaged()) {
			imagemProdutoHome.removerImagem(imagemProduto);
		}
		getInstance().getImagensProduto().remove(imagemProduto);
		imagensProduto.remove(imagemProduto);
		imagemToShow = null;
	}

	public List<ImagemProduto> getImagensProduto() {
		return imagensProduto;
	}

	public void setImagensProduto(List<ImagemProduto> imagensProduto) {
		this.imagensProduto = imagensProduto;
	}

	@Override
	@Transactional
	public String persist() {
		String result = super.persist();
		return result;
	}
	
	/**
	 * Atualiza o preço de todos os lotes de produto
	 * para o preço informado.
	 */
	public String atualizarPrecoTodosLotes(){
		
		StringBuilder query = new StringBuilder(
		"update LoteProduto lote set lote.precoVendaUnidade = :preco WHERE cod_produto = :cod ");
		
		Query createQuery = getEntityManager().createQuery(query.toString());
		createQuery.setParameter("preco", getInstance().getPrecoVendaDefault());
		createQuery.setParameter("cod", getInstance().getCodProduto());
		int updated = createQuery.executeUpdate();
		
		addFacesMessage("Preços atualizados com sucesso. Quantidade de lotes atualizados: " + updated , "");
		return update();
	}

	/*
	 * Retorna o Array de Bytes do arquivo selecionado pelo usuário para upload
	 * 
	 * @param file Arquivo
	 * 
	 * @return byte[]
	 */
	private byte[] getData(File file) {
		InputStream in = null;
		byte[] result = null;
		try {
			in = new FileInputStream(file);
			BufferedInputStream buffer = new BufferedInputStream(in);
			result = new byte[buffer.available()];
			buffer.read(result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return result;
	}

	@Override
	public String update() {
		
		Produto produto = getInstance();
		
		String log = LogUtil.logHistorico("O produto codigo "
				+ produto.getCodProduto() + " - "
				+ produto.getCodProdutoExterno() + " - "
				+ produto.getNomeProduto() + " teve seus dados atualizados.");
		
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);

		return super.update();
	}
	
	public void selecionarImagemToShow(ImagemProduto imagemProduto){
		imagemToShow = imagemProduto;
	}
	
	public void mostrarImagem(OutputStream stream, Object object) throws IOException{
		ImagemProduto im = (ImagemProduto)object;
		im.setConteudoImagem(imagemProdutoHome.buscarConteudoImagem(im));
		if (im.getConteudoImagem()!=null){
			stream.write(im.getConteudoImagem().getBytes());
		}
	}

}
