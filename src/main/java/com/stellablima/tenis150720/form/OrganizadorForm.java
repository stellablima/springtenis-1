package com.stellablima.tenis150720.form;


import javax.validation.constraints.NotEmpty;

import com.stellablima.tenis150720.model.Organizador;
import com.stellablima.tenis150720.repository.OrganizadorRepository;

public class OrganizadorForm {

	@NotEmpty(message = "Digite um email cadastrado")
	private String email;
	@NotEmpty(message = "Digite uma senha cadastrada")
	private String senha;
	
	
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
	public Organizador converter(OrganizadorRepository or) {
		Organizador organizador = or.findByEmail(email);
		return organizador;
	}
	
	
}
