package com.stellablima.tenis150720.form;


import javax.validation.constraints.NotEmpty;

public class OrganizadorFormUpdate {

	//long id;
	@NotEmpty(message = "Digite um nome")
	private String nome;
	@NotEmpty(message = "Digite uma senha")
	private String senha;
	
	
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
	//public Organizador converter(OrganizadorRepository or) {
	//	Organizador organizador = or.findById(id);
	//	return organizador;
	//}
	
	
}
