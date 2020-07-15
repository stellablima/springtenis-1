package com.stellablima.tenis150720.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;



@Entity 
public class Organizador implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
	private long id;
	
	@NotEmpty
	@Column(length = 30, nullable = false)
	private String nome;
	
	@NotEmpty
	@Column(length = 14, nullable = false, unique = true)
	private String cpf;
	
	@NotEmpty
	@Column(length = 30, nullable = false, unique = true)
	private String email;
	
	@NotEmpty
	@Column(length = 15, nullable = false) //@Size(min=2, max=100, message="Tem de ter pelo menos 2 letras") 
	private String senha;
	
	//@Column furutamente pra manyToMany colocar comando a baixo no clube, e tornar organizador lista, depois trazer o @joinColum de la pra cá
	@OneToOne 
	@JoinColumn
	private Clube clube;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Clube getClube() {
		return clube;
	}
	public void setClube(Clube clube) {
		this.clube = clube;
	}
	
}
