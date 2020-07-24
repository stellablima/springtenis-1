package com.stellablima.tenis150720.controller;


import java.text.ParseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.stellablima.tenis150720.model.Atleta;
import com.stellablima.tenis150720.model.Organizador;
import com.stellablima.tenis150720.model.Torneio;
import com.stellablima.tenis150720.repository.AtletaRepository;
import com.stellablima.tenis150720.repository.OrganizadorRepository;
import com.stellablima.tenis150720.repository.TorneioRepository;


@Controller
@RequestMapping("{id_organizador}/{id_torneio}/chaves")
public class PartidaController {
	@Autowired
	OrganizadorRepository or;

	@Autowired
	AtletaRepository ar;
	@Autowired
	TorneioRepository tr;
	
	@RequestMapping("/view")
	public ModelAndView listChaves(@PathVariable("id_organizador") long id_organizador, @PathVariable("id_torneio") long id_torneio) throws ParseException {
		ModelAndView mv = new ModelAndView("chaves");
		Organizador organizador = or.findById(id_organizador);
		mv.addObject("organizador", organizador);  		
		Torneio torneio = tr.findById(id_torneio);
		mv.addObject("torneio", torneio); 		
		List<Atleta> atletas1 = torneio.getAtletasParticipantes();	
		mv.addObject("atletas1", atletas1);

		return mv;
	}

	@RequestMapping("/vencedor")
	public String saveAtletaVencedor(@PathVariable("id_organizador") long id_organizador, @PathVariable("id_torneio") long id_torneio, long codigoAtleta){
	Torneio torneio = tr.findById(id_torneio);
	torneio.setAtletaVencedor(ar.findById(codigoAtleta));
	torneio.setAtivo(false);
	tr.save(torneio);
		return "redirect:view";
	}
}
