package br.com.gerenciadornet.util;

import java.util.List;

import br.com.gerenciadornet.entity.Produto;

public class MigracaoEcommerceWPress {
	
	static boolean imprimir = true;
	
	
	/**
	 * Método que gera um script para inserir produtos do GN no Wordpress ecommerce.
	 * Passo a passo:
	 * 1 - Incluir o método MigracaoEcommerceWPress.gerarScriptProdutos(produtoList) na lista de resultados: getResultList
	 * 2 - copiar os valores dos ids das tabelas wp_posts e wp_postmeta e somar + 1 para atribuir aos id_wp_posts  e id_wp_postmeta
	 * 2 - Realizar uma pesquisa dos produtos que serão migrados
	 * 3 - Copiar o script gerado em console
	 * 6 - Excecutar o script gerado no banco de dados de produção
	 * 
	 * @param produtoList
	 */
	public static void gerarScriptProdutos(List<Produto> produtoList){
		
				
		//copiar o valor do id da tabela e adicionar +1
		int id_wp_posts = 3208;
		int id_wp_postmeta = 62987;
			
		
		System.out.println("\n\n\n -- Iniciando criação de script de exportação de produtos ----- ");
		
		for (Produto produto : produtoList) {
			
			System.out.println(""
					+ "\nINSERT INTO `wp_posts` ("
					+ "`id`,"
					+ "`post_author`,"
					+ "`post_date`,"
					+ "`post_date_gmt`,"
					+ "`post_content`,"
					+ "`post_title`,"
					+ "`post_excerpt`,"
					+ "`post_status`,"
					+ " `comment_status`,"
					+ "`ping_status`,"
					+ "`post_password`,"
					+ "`post_name`,"
					+ "`to_ping`,"
					+ "`pinged`,"
					+ "`post_modified`,"
					+ "`post_modified_gmt`,"
					+ "`post_content_filtered`,"
					+ "`post_parent`,"
					+ "`guid`,"
					+ "`menu_order`,"
					+ "`post_type`,"
					+ "`post_mime_type`,"
					+ "`comment_count`) VALUES"
					+ "("
					+ "'" + id_wp_posts + "',"
					+ "'1',"
					+ "CURRENT_TIMESTAMP,"
					+ "CURRENT_TIMESTAMP,"
					+ "'"+ produto.getNomeProduto()+ "',"
					+ "'"+ produto.getNomeProduto()+ "',"
					+ "'"+ produto.getNomeProduto()+ "',"
					+ "'publish',"
					+ "'open',"
					+ "'closed', "
					+ "'',"
					+ "'produto-"+ id_wp_posts + "',"
					+ "'',"
					+ "'',"
					+ "CURRENT_TIMESTAMP,"
					+ "CURRENT_TIMESTAMP,"
					+ "'',"
					+ "'0',"
					+ "'http://www.dentaldf.com.br/ecommerce/?post_type=product&#038;p=" + id_wp_posts + "',"
					+ "'0',"
					+ "'product',"
					+ "'',"
					+ "'0'"		
					+ ");"
					);
			
			System.out.println(
					"INSERT INTO `wp_postmeta` (`meta_id`, `post_id`, `meta_key`, `meta_value`) VALUES" +
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'custom_page_title', ''),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'laborator_meta_description', ''),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'laborator_meta_keywords', '"+ produto.getCategoria()  +"'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'laborator_meta_robots_index', ''),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'laborator_meta_robots_follow', ''),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_vc_post_settings', 'a:1:{s:10:\"vc_grid_id\";a:0:{}}'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_edit_lock', '1480960679:1'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_edit_last', '1'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_visibility', 'visible'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_stock_status', 'instock'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_thumbnail_id', ''),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'total_sales', '0'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_downloadable', 'no'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_virtual', 'no'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_tax_status', 'taxable'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_tax_class', ''),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_purchase_note', ''),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_featured', 'no'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_weight', '"+ produto.getPeso()  +"'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_length', ''),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_width', '"+ produto.getLargura()  +"'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_height', '"+ produto.getAltura()  +"'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_sku','" + produto.getCodProduto() + "'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_product_attributes', 'a:0:{}'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_regular_price', ' " + produto.getPrecoVendaDefault() + "'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_sale_price', ''),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_sale_price_dates_from', ''),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_sale_price_dates_to', ''),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_price', ' " + produto.getPrecoVendaDefault() + "'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_sold_individually', ''),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_manage_stock', ''),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_backorders', 'no'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_stock', " + "0" +  "),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_upsell_ids', 'a:0:{}'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_crosssell_ids', 'a:0:{}'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_product_version', '2.6.7'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_product_image_gallery', ''),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'slide_template', 'default'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_wc_rating_count', 'a:0:{}'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_wc_rating_count', 'a:0:{}'),"+
					"(" + (id_wp_postmeta++) +"," + id_wp_posts + "," + "'_wc_average_rating', '0');"
				);
				
			System.out.println(
						"INSERT INTO `wp_term_relationships` (`object_id`, `term_taxonomy_id`, `term_order`) VALUES ('" + id_wp_posts + "' ,'4','0');");
			id_wp_posts++;
		}
		
		//term_taxonomy_id = 4 (product_type)//TIPO PRODUTO 
		System.out.println("\nupdate wp_term_taxonomy as wp_term_taxonomy, ("
				+ "SELECT  t.count as contador FROM wp_term_taxonomy t where t.term_taxonomy_id = 4) as contadorSelecionado "
				+ "set wp_term_taxonomy.count = contadorSelecionado.contador + " + produtoList.size() + " where wp_term_taxonomy.term_taxonomy_id = 4;");
		
		System.out.println("\n\n\n -- ##  FIM  criação de script de exportação de produtos ----- ");
		imprimir = false;
	}

	
	public static void gerarCSVProdutos(List<Produto> produtoList){
		
		
		if(!imprimir)
			return;
		
		int id_wp_posts = 2443;
		//int id_wp_postmeta = 42174;
		
		StringBuilder cabecalho = new StringBuilder();
		cabecalho.append("version;title;link;description;pubDate;language;ns2:wxr_version;ns2:base_site_url;ns2:base_blog_url;"
				+ "ns2:author_id;ns2:author_login;ns2:author_email;ns2:author_display_name;ns2:author_first_name;"
				+ "ns2:author_last_name;generator;title2;link3;pubDate4;ns4:creator;guid;isPermaLink;description5;"
				+ "ns3:encoded;ns1:encoded;ns2:post_id;ns2:post_date;ns2:post_date_gmt;ns2:comment_status;ns2:ping_status;"
				+ "ns2:post_name;ns2:status;ns2:post_parent;ns2:menu_order;ns2:post_type;ns2:post_password;ns2:is_sticky;category;"
				+ "domain;nicename;ns2:meta_key;ns2:meta_value"
				);
		
		System.out.println("\n\n\n -- Iniciando criação de CSV de exportação de produtos ----- ");
		
		System.out.println(cabecalho.toString());
		
		for (Produto produto : produtoList) {
			
			String linhaBase = 
					"2;"
					+ "Dental DF;"
					+ "http://www.dentaldf.com.br/ecommerce;"
					+ "Dental DF produtos Odontologicos;"
					+ "Mon, 09 Jan 2017 10:00:00 +0000;"
					+ "pt-BR;"
					+ "1.2;"
					+ "http://www.dentaldf.com.br/ecommerce;"
					+ "http://www.dentaldf.com.br/ecommerce;"
					+ ";"
					+ ";"
					+ ";"
					+ ";"
					+ ";"
					+ ";"
					+ "https://wordpress.org/?v=4.6.1;"
					+ produto.getNomeProduto() + ";"
					+ "http://www.dentaldf.com.br/ecommerce/produto/" +  produto.getNomeProduto().replace(' ', '-').toLowerCase() + "/;"
					+ "Mon, 09 Jan 2017 10:00:00 +0000;"
					+ "loja;"
					+ "http://www.dentaldf.com.br/ecommerce/?post_type=product&p="+ id_wp_posts +";"
					+ ";"
					+ ";"
					+ produto.getNomeProduto() + ";"
					+ produto.getNomeProduto() + ";"
					+  id_wp_posts+";"
					+ "2017-01-09 10:00:00;"
					+ "2017-01-09 10:00:00;"
					+ "open;"
					+ "closed;"
					+  produto.getNomeProduto().replace(' ', '-').toLowerCase() + ";"
					+ "publish;"
					+ "0;"
					+ "0;"
					+ "product;"
					+ ";"
					+ "0;";
			
			
			String linhaBasePost ="";
			String categoria = "";
			
			if (produto.getCategoria() != null){
				
				linhaBasePost = produto.getCategoria().getNomeCategoria() +";"
						+ "product_cat;"
						+ produto.getCategoria().getNomeCategoria() + "-" + produto.getMarca().getNomeMarca() + ";"
						+ ";" 
						+ ";" ;
				categoria = produto.getCategoria().getNomeCategoria();
				
			} else {
				linhaBasePost = ";product_cat;;"
						+ ";" 
						+ ";" ;
			}
					
			
					
			System.out.println(linhaBase + linhaBasePost);
					
			String linhaBasePost1  = "simple;"
								+ "product_type;"
								+ "simple"
								+ ";" 
								+ ";" ;
					
			System.out.println(linhaBase + linhaBasePost1);
			
			String linhaBasePost2  =  produto.getNomeProduto() + ";"
								+ "product_tag;"
								+ produto.getNomeProduto() + ";"
								+ ";" 
								+ ";" ;
					
			System.out.println(linhaBase + linhaBasePost2);

			linhaBase += ";;;";
			
			String produtoPeso = "";
			
			 if(produto.getPeso() != null){
				 produtoPeso = ""+produto.getPeso() ;
			 }
			 
			 String produtoWidth = "";
				
			 if(produto.getLargura() != null){
				 produtoWidth = ""+produto.getLargura() ;
			 }
			 
			 String produtoheight = "";
				
			 if(produto.getLargura() != null){
				 produtoheight = ""+produto.getAltura() ;
			 }
			
			System.out.println(linhaBase + "custom_page_title;");
			System.out.println(linhaBase + "laborator_meta_description;");
			System.out.println(linhaBase + "laborator_meta_keywords;"+ categoria);
			System.out.println(linhaBase + "laborator_meta_robots_index;");
			System.out.println(linhaBase + "laborator_meta_robots_follow;");
			//Depois que jogar no excel, substituir XXXX pelo valor abaixo
			//a:1:{s:10:"vc_grid_id";a:0:{}} 			
			System.out.println(linhaBase + "_vc_post_settings;XXXX");
			System.out.println(linhaBase + "_edit_last;1");
			System.out.println(linhaBase + "_visibility;visible");
			System.out.println(linhaBase + "_stock_status;instock");
			System.out.println(linhaBase + "_thumbnail_id;");
			System.out.println(linhaBase + "total_sales;0");
			System.out.println(linhaBase + "_downloadable;no");
			System.out.println(linhaBase + "_virtual;no");
			System.out.println(linhaBase + "_tax_status;shipping");
			System.out.println(linhaBase + "_tax_class; ");
			System.out.println(linhaBase + "_purchase_note; ");
			System.out.println(linhaBase + "_featured;no");
			System.out.println(linhaBase + "_weight;"+ produtoPeso);
			System.out.println(linhaBase + "_length;");
			System.out.println(linhaBase + "_width;" + produtoWidth);
			System.out.println(linhaBase + "_height;" + produtoheight);
			System.out.println(linhaBase + "_sku;" + produto.getCodProduto() );
			System.out.println(linhaBase + "_product_attributes;a:0:{}");
			System.out.println(linhaBase + "_regular_price;" + produto.getPrecoVendaDefault() );
			System.out.println(linhaBase + "_sale_price;");
			System.out.println(linhaBase + "_sale_price_dates_from;");
			System.out.println(linhaBase + "_sale_price_dates_to;");
			System.out.println(linhaBase + "_price; " + produto.getPrecoVendaDefault() );
			System.out.println(linhaBase + "_sold_individually;");
			System.out.println(linhaBase + "_manage_stock;no");
			System.out.println(linhaBase + "_backorders;no");
			System.out.println(linhaBase + "_stock;0");
			System.out.println(linhaBase + "_upsell_ids;a:0:{}");
			System.out.println(linhaBase + "_crosssell_ids;a:0:{}");
			System.out.println(linhaBase + "_product_version;2.6.7");
			System.out.println(linhaBase + "_product_image_gallery;");
			System.out.println(linhaBase + "slide_template;default");
			System.out.println(linhaBase + "_wc_rating_count;a:0:{}");
			System.out.println(linhaBase + "_wc_average_rating;0");
			System.out.println(linhaBase + "_wc_review_count;0");
			
			id_wp_posts++;
		}
		
		System.out.println("\n\n\n -- ##  FIM  criação de script de exportação de produtos ----- ");
		imprimir = false;
	}

	
	public static void main(String[] args) {
		gerarCSVProdutos(null);
	}

}
