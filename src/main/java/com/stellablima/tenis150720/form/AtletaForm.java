package com.stellablima.tenis150720.form;

import java.util.Optional;

import com.stellablima.tenis150720.model.Atleta;
import com.stellablima.tenis150720.model.Clube;
import com.stellablima.tenis150720.repository.ClubeRepository;



public class AtletaForm {
	//@NotNull @NotEmpty @Length(min = 3)
	private String nome;
	
	//@NotNull @NotEmpty 
	private String dataNascimento;
	
	//@NotNull @NotEmpty
	private Long clube_id;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Long getNomeClube() {
		return clube_id;
	}

	public void setNomeClube(Long clube_id) {
		this.clube_id = clube_id;
	}
	
	public Atleta converter(ClubeRepository clubeRepository) {
		Optional<Clube> clube = clubeRepository.findById(clube_id);
		return new Atleta(nome, dataNascimento, clube.get());
	}

}
