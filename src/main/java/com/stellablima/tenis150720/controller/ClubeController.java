package com.stellablima.tenis150720.controller;

//OKstella_front

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.stellablima.tenis150720.bussiness.Convert;
import com.stellablima.tenis150720.form.OrganizadorFormLogin;
import com.stellablima.tenis150720.form.OrganizadorFormUpdate;
import com.stellablima.tenis150720.model.Atleta;
import com.stellablima.tenis150720.model.Clube;
import com.stellablima.tenis150720.model.Organizador;
import com.stellablima.tenis150720.model.Torneio;
import com.stellablima.tenis150720.repository.ClubeRepository;
import com.stellablima.tenis150720.repository.OrganizadorRepository;
import com.stellablima.tenis150720.repository.TorneioRepository;


@Controller
@RequestMapping("/{id_organizador}/clube")
public class ClubeController {

	@Autowired
	private OrganizadorRepository or;
	@Autowired
	private ClubeRepository cr;
	@Autowired
	private TorneioRepository tr; 
	
	@RequestMapping("/editar")
	public ModelAndView formClube(@PathVariable("id_organizador") long id_organizador) throws ParseException{
		ModelAndView mv = new ModelAndView("Clube");
		Organizador organizador = or.findById(id_organizador);
		mv.addObject("organizador",organizador);

		Clube clube = organizador.getClube();
		
		List<Organizador> organizadores = clube.getOrganizadores();
		mv.addObject("organizadores",organizadores);
		
		mv.addObject("clube",clube);
		Iterable<Torneio> torneios = clube.getTorneios();
		mv.addObject("torneios", Convert.ordenarTorneioDataBS(torneios));
		List<Torneio> torneiosVencidos = new ArrayList<Torneio>();
		for(Torneio torneio : tr.findAll()){
			Optional<Atleta> av = Optional.ofNullable(torneio.getAtletaVencedor());
			//se torneio tem um vencedor
			if(av.isPresent() && av.get().getClube()==clube) {
				torneiosVencidos.add(torneio);
			}			
		}
		mv.addObject("torneiosVencidos", torneiosVencidos);
		return mv;	
	}


	@PostMapping("/editar")
	public String updateClube(@PathVariable("id_organizador") long id_organizador, @Valid Clube clube, BindingResult result, @Valid OrganizadorFormUpdate organizador, BindingResult result2, RedirectAttributes attributes){
		if(result.hasErrors() || result2.hasErrors()){ //campos vazios
            attributes.addFlashAttribute("mensagem", "Verifique os campos"); 
            return "redirect:/{id_organizador}/clube/editar";
        }
		//BUSCAR CNPJ NO BANCO, SE REPETIDO
		//BUSCAR EMAIL NO BANCO, SE REPETIDO
		List<Clube> clubes = cr.findAll();
		clubes.remove(or.findById(id_organizador).getClube());
		for(Clube clubeBanco : clubes) {
			if(clubeBanco.getCnpj().intern()==clube.getCnpj().intern()) {
				attributes.addFlashAttribute("mensagem", "CNPJ já cadastrado");
				return "redirect:/{id_organizador}/clube/editar";
			}
			if(clubeBanco.getEmailClube().intern()==clube.getEmailClube().intern()) {
				attributes.addFlashAttribute("mensagem", "Email já cadastrado");
	            return "redirect:/{id_organizador}/clube/editar";
			}	
		}
		//updateClubeService(clube, or.findById(id_organizador).getClube().getId(), organizador, id_organizador);	
		Clube clubeUpdate = or.findById(id_organizador).getClube();
		clubeUpdate.setCnpj(clube.getCnpj());
		clubeUpdate.setEmailClube(clube.getEmailClube());
		clubeUpdate.setEndereco(clube.getEndereco());
		clubeUpdate.setNomeClube(clube.getNomeClube());
		//clubeUpdate.setOrganizador(organizadorUpdate.getId()); no caso many to one
		
		Organizador organizadorUpdate = or.findById(id_organizador); 
		//organizadorUpdate.setCpf(organizador.getCpf());
		//organizadorUpdate.setEmail(organizador.getEmail());
		organizadorUpdate.setNome(organizador.getNome());
		organizadorUpdate.setSenha(organizador.getSenha());
		//organizadorUpdate.get().setClube(clubeUpdate.getId());
		
		//or.save(organizadorUpdate); o clube ja vai puxar o organizador
		cr.save(clubeUpdate);	
		
		return "redirect:/{id_organizador}/torneio/view";
	}

}
	
