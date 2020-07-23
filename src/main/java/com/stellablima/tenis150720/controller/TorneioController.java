package com.stellablima.tenis150720.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.stellablima.tenis150720.bussiness.Convert;
import com.stellablima.tenis150720.model.Atleta;
import com.stellablima.tenis150720.model.Organizador;
import com.stellablima.tenis150720.model.Torneio;
import com.stellablima.tenis150720.repository.AtletaRepository;
import com.stellablima.tenis150720.repository.OrganizadorRepository;
import com.stellablima.tenis150720.repository.TorneioRepository;

@Controller
@RequestMapping("{id_organizador}/torneio")
public class TorneioController {
	@Autowired
	private TorneioRepository tr;
	@Autowired
	private OrganizadorRepository or;
	@Autowired
	private AtletaRepository ar;

	
	@GetMapping(value = "/view")
	public ModelAndView listTorneios(@PathVariable("id_organizador") long id_organizador) throws ParseException {
		ModelAndView mv = new ModelAndView("index");
		Organizador organizador = or.findById(id_organizador);
		mv.addObject("organizador", organizador);
		List<Torneio> torneios = tr.findByClube(organizador.getClube()); //retirei iterable do repository e dessa linha
		mv.addObject("torneios", Convert.ordenarTorneioDataBS(torneios));
		return mv;
	}
	
	@RequestMapping("/cadastrar")
	public ModelAndView formCadastroTorneio(@PathVariable("id_organizador") long id_organizador) {
		ModelAndView mv = new ModelAndView("formTorneio");
		Organizador organizador = or.findById(id_organizador);
		mv.addObject("organizador", organizador);
		return mv;
	}
	@PostMapping("/cadastrar")
	public String saveTorneio(@PathVariable("id_organizador") long id_organizador, @Valid Torneio torneio,
			BindingResult result, RedirectAttributes attributes) throws ParseException {
		//Campo nome ou data vazio
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos");
			return "redirect:/{id_organizador}/torneio/cadastrar";
		}
		//verificação data
		Calendar cal = Convert.convertStringToCalendar("yyyy-MM-dd", torneio.getData_inicio());
		String dataTorneio = Convert.convertCalendarToString("yyyyMMdd", cal);		
		Calendar today = Calendar.getInstance();
		String dataHoje = Convert.convertCalendarToString("yyyyMMdd", today);
		if( Integer.parseInt(dataTorneio)< Integer.parseInt(dataHoje)) {
			attributes.addFlashAttribute("mensagem", "Torneio so pode ser cadastrado para uma data futura");
			return "redirect:/{id_organizador}/torneio/cadastrar";
		}
		//saveTorneioService(id_organizador, torneio);
		Organizador organizador = or.findById(id_organizador);
		torneio.setClube(organizador.getClube());
		tr.save(torneio);
		return "redirect:/{id_organizador}/torneio/view";
	}

	@GetMapping("/deletar")
	public String deleteTorneio(@PathVariable("id_organizador") long id_organizador, long id_torneio) {
		deleteTorneioService(id_torneio);
		return "redirect:/{id_organizador}/torneio/view";
	}
	@ResponseBody
	@DeleteMapping("/deletar/{id_torneio}")
	private void deleteTorneioService(@PathVariable("id_torneio") long id_torneio) {
		Torneio torneio = tr.findById(id_torneio);
		tr.delete(torneio);
	}

	@RequestMapping("/editar/{id_torneio}")
	public ModelAndView formEditarTorneio(@PathVariable("id_organizador") long id_organizador,
			@PathVariable("id_torneio") long id_torneio) throws ParseException {
		ModelAndView mv = new ModelAndView("Torneio");
		Organizador organizador = or.findById(id_organizador);
		mv.addObject("organizador", organizador);
		Torneio torneio = tr.findById(id_torneio);
		mv.addObject("torneio", torneio);
		List<Atleta> atletas1 = torneio.getAtletasParticipantes();
		mv.addObject("atletas1", atletas1);
		List<Atleta> atletas2 = ar.findAll();
		atletas2.removeAll(atletas1);
		List<Atleta> atletaView = new ArrayList<Atleta>() ;
        for(Atleta atleta : atletas2) {     	
        	atleta.setDataNascimento(Convert.calculaIdade("yyyy-MM-dd", atleta.getDataNascimento()));
        	atletaView.add(atleta);
        }
		mv.addObject("atletas2", atletaView);		
		return mv;
	}
	@PostMapping("/editar/{id_torneio}")
	public String updateTorneio(@PathVariable("id_organizador") long id_organizador,
			@PathVariable("id_torneio") long id_torneio, @Valid Torneio torneio, BindingResult result,
			RedirectAttributes attributes) throws ParseException {
		//se campo vazio
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos");
			return "redirect:/{id_organizador}/torneio/editar/{id_torneio}";
		}
		//verificação data
		Calendar cal = Convert.convertStringToCalendar("yyyy-MM-dd", torneio.getData_inicio());
		String dataTorneio = Convert.convertCalendarToString("yyyyMMdd", cal);		
		Calendar today = Calendar.getInstance();
		String dataHoje = Convert.convertCalendarToString("yyyyMMdd", today);
		if( Integer.parseInt(dataTorneio)< Integer.parseInt(dataHoje)) {
			attributes.addFlashAttribute("mensagem", "O torneio so pode ser remarcado para uma data futura");
			return "redirect:/{id_organizador}/torneio/editar/{id_torneio}";
		}
		//updateTorneioService(id_torneio, torneio);		
		Torneio torneioUpdated = tr.findById(id_torneio);
		torneioUpdated.setData_inicio(torneio.getData_inicio());
		torneioUpdated.setNome(torneio.getNome());
		tr.save(torneioUpdated);
		return "redirect:/{id_organizador}/torneio/view";
	}

	@RequestMapping("/editar/{id_torneio}/adicionar-atleta/{id_atleta}")
	public String saveAtletaTorneio(@PathVariable("id_organizador") long id_organizador,
			@PathVariable("id_torneio") long id_torneio, @PathVariable("id_atleta") long id_atleta, RedirectAttributes attributes) {
		Torneio torneio = tr.findById(id_torneio);
		//limitação de 16 atletas por torneio
		int count = torneio.getAtletasParticipantes().size();
		if(count > 16 -1) {
			attributes.addFlashAttribute("mensagem", "Vagas excedidas");
			return "redirect:/{id_organizador}/torneio/editar/{id_torneio}";
		}
		//saveAtletaTorneioService(id_torneio, id_atleta); 
		Atleta atleta = ar.findById(id_atleta);
		List<Atleta> atletas = torneio.getAtletasParticipantes();
			//logica se atleta ja no torneio, desnecessaria no front //for(Atleta atletaLoop: atletas) {if(atletaLoop.equals(atleta)) {attributes.addFlashAttribute("mensagem", "Atleta existente!");return "redirect:/{id_organizador}/torneio/editar/{id_torneio}";}}
		atletas.add(atleta);
		torneio.setAtletasParticipantes(atletas);
		tr.save(torneio);
		return "redirect:/{id_organizador}/torneio/editar/{id_torneio}";
	}

	@GetMapping("/editar/{id_torneio}/deletar-atleta") //a propria doc mandou usar get nesses casos nada mais faz sentido ;-;
	public String deleteAtletaTorneio(@PathVariable("id_organizador") long id_organizador,
			@PathVariable("id_torneio") long id_torneio, long id_atleta) {
		deleteAtletaTorneioService(id_torneio, id_atleta);
		return "redirect:/{id_organizador}/torneio/editar/{id_torneio}";
	}
	@ResponseBody
	@DeleteMapping("/editar/{id_torneio}/deletar-atleta/{id_atleta}")
	private void deleteAtletaTorneioService(@PathVariable("id_torneio") long id_torneio, @PathVariable("id_atleta") long id_atleta) {
		Torneio torneio = tr.findById(id_torneio);
		Atleta atleta = ar.findById(id_atleta);
		List<Atleta> atletas = torneio.getAtletasParticipantes();
		atletas.remove(atleta);
		tr.save(torneio);
	}

}
