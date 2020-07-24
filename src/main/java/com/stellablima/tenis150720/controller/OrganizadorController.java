package com.stellablima.tenis150720.controller;


import java.text.ParseException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stellablima.tenis150720.bussiness.Convert;
import com.stellablima.tenis150720.model.Organizador;
import com.stellablima.tenis150720.model.Torneio;
import com.stellablima.tenis150720.repository.OrganizadorRepository;

@Controller
@RequestMapping("/{id_organizador}/organizador")
public class OrganizadorController {
	@Autowired
	private OrganizadorRepository or;
	
	@RequestMapping("/editar")
	public ModelAndView formOrganizador(@PathVariable("id_organizador") long id_organizador) throws ParseException {
		ModelAndView mv = new ModelAndView("Organizador");
		Organizador organizador = or.findById(id_organizador);
		mv.addObject("organizador",organizador);
		Iterable<Torneio> torneios = organizador.getClube().getTorneios();
		mv.addObject("torneios", Convert.ordenarTorneioDataBS(torneios));
		return mv;		
	}

	@PostMapping("/editar")
	public String updateOrganizador(@PathVariable("id_organizador") long id_organizador, @Valid Organizador organizador, BindingResult result, RedirectAttributes attributes){
		if(result.hasErrors()){ //se campo vazio
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            return "redirect:/{id_organizador}/organizador/editar";
        }
		//BUSCAR EMAIL NO BANCO, SE REPETIDO
		//BUSCAR CPF NO BANCO, SE REPETIDO
		List<Organizador> organizadores = or.findAll();
				organizadores.remove(or.findById(id_organizador)); //VERIFICAR ESSA LOGICA, porque ele pode cadastrar com o mesmo cpf/email o organizador do proprio clube, caso outro organizador de outro clube quiser se atualizar ou cadastrar com o cpf de OUTRO organizador ele nao consegue
		for(Organizador organizadorBanco : organizadores) {
			if(organizadorBanco.getCpf().intern()==organizador.getCpf().intern()) {
				attributes.addFlashAttribute("mensagem", "CPF já cadastrado");
	            return "redirect:/{id_organizador}/organizador/editar";
			}
			if(organizadorBanco.getEmail().intern()==organizador.getEmail().intern()) {
				attributes.addFlashAttribute("mensagem", "Email já cadastrado");
	            return "redirect:/{id_organizador}/organizador/editar";
			}	
		}
		//updateOrganizadorService(organizador, id_organizador);	
		Organizador organizadorUpdate = or.findById(id_organizador); 
		organizadorUpdate.setCpf(organizador.getCpf());
		organizadorUpdate.setEmail(organizador.getEmail());
		organizadorUpdate.setNome(organizador.getNome());
		organizadorUpdate.setSenha(organizador.getSenha());
		//organizadorUpdate.get().setClube(clubeUpdate.getId());
		
		or.save(organizadorUpdate);
		
		return "redirect:/{id_organizador}/torneio/view";
	}
	@RequestMapping("/cadastrar")
	public ModelAndView formCadastroOrganizador(@PathVariable("id_organizador") long id_organizador) {
		ModelAndView mv = new ModelAndView("formOrganizador");
		Organizador organizador = or.findById(id_organizador);
		mv.addObject("organizador",organizador);
		return mv;		
	}
	@PostMapping("/cadastrar")
	public String saveOrganizador(@PathVariable("id_organizador") long id_organizador, @Valid Organizador organizador, BindingResult result, RedirectAttributes attributes){
		if(result.hasErrors()){ //se campo vazio
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            return "redirect:/{id_organizador}/organizador/cadastrar";
        }
		//BUSCAR EMAIL NO BANCO, SE REPETIDO
		//BUSCAR CPF NO BANCO, SE REPETIDO
		List<Organizador> organizadores = or.findAll();
		for(Organizador organizadorBanco : organizadores) {
			if(organizadorBanco.getCpf().intern()==organizador.getCpf().intern()) {
				attributes.addFlashAttribute("mensagem", "CPF já cadastrado");
	            return "redirect:/{id_organizador}/organizador/cadastrar";
			}
			if(organizadorBanco.getEmail().intern()==organizador.getEmail().intern()) {
				attributes.addFlashAttribute("mensagem", "Email já cadastrado");
	            return "redirect:/{id_organizador}/organizador/cadastrar";
			}	
		}
		organizador.setClube(or.findById(id_organizador).getClube());
		or.save(organizador);
		return "redirect:/{id_organizador}/torneio/view";
	}
	//cRuD DE ORGANIZADOR SOZINHO formOrganizador
}












