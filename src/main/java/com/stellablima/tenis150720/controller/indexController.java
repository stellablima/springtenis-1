package com.stellablima.tenis150720.controller;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.stellablima.tenis150720.form.OrganizadorFormLogin;
import com.stellablima.tenis150720.model.Clube;
import com.stellablima.tenis150720.model.Organizador;
import com.stellablima.tenis150720.repository.ClubeRepository;
import com.stellablima.tenis150720.repository.OrganizadorRepository;

@Controller
public class indexController {
	@Autowired
	private OrganizadorRepository or;
	@Autowired
	private ClubeRepository cr;
	//@OnError //é possivel ter tela de /erro ?
	//public String error() { return("login");}
	
	@RequestMapping("/") 
	public String index() { return("redirect:/login");}		

	@RequestMapping("/login") //formLogin
	public String login() { return("login");}
	@PostMapping(value = "/login")
	public String findOrganizador(@Valid OrganizadorFormLogin organizador, BindingResult result, RedirectAttributes attributes){
		if(result.hasErrors()){ //campo email ou senha vazio
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            return "redirect:/login";
        }
		Optional<Organizador> organizador1 = Optional.ofNullable(or.findByEmail(organizador.getEmail()));
		if(organizador1.isPresent() && 
				organizador1.get().getSenha().intern()==organizador.getSenha().intern()) { 
			return "redirect:/"+ organizador1.get().getId() +"/torneio/view";
		}
		else {
			attributes.addFlashAttribute("mensagem", "Organizador não encontrado");
			return "redirect:/login";
		}
		
	}
	
	@RequestMapping("/signup") //formClube
	public String signup() { return "formClube";}
	@PostMapping("/signup") 
	public String saveClubeOrganizador(@Valid Clube clube, BindingResult result, @Valid Organizador organizador, BindingResult result2, RedirectAttributes attributes){
		if(result.hasErrors() || result2.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            return ("redirect:/clube/signup");
        }
		//BUSCAR EMAIL NO BANCO, SE REPETIDO
		//BUSCAR CNPJ NO BANCO, SE REPETIDO
		List<Clube> clubes = cr.findAll();
		for(Clube clubeBanco : clubes) {
			if(clubeBanco.getCnpj().intern()==clube.getCnpj().intern()) {
				attributes.addFlashAttribute("mensagem", "CNPJ já cadastrado");
				return ("formClube");
			}
			if(clubeBanco.getEmailClube().intern()==clube.getEmailClube().intern()) {
				attributes.addFlashAttribute("mensagem", "Email de clube já cadastrado");
	            return ("redirect:/clube/signup");
			}	
		}
		//BUSCAR EMAIL NO BANCO, SE REPETIDO
		//BUSCAR CPF NO BANCO, SE REPETIDO
		List<Organizador> organizadores = or.findAll();
		for(Organizador organizadorBanco : organizadores) {
			if(organizadorBanco.getCpf().intern()==organizador.getCpf().intern()) {
				attributes.addFlashAttribute("mensagem", "CPF já cadastrado");
	            return  ("redirect:/clube/signup"); 
			}
			if(organizadorBanco.getEmail().intern()==organizador.getEmail().intern()) {
				attributes.addFlashAttribute("mensagem", "Email de organizador já cadastrado");
	            return  ("redirect:/clube/signup");
			}	
		}
		//saveClubeService(clube, organizador);	
		cr.save(clube);	
		organizador.setClube(clube);
		or.save(organizador);
		return ("redirect:/"+ organizador.getId() +"/torneio/view");
	}
	
	
}
