package com.stellablima.tenis150720.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("{id_organizador}/atleta")
public class AtletaController {

	@Autowired
	private AtletaRepository ar;
	@Autowired
	OrganizadorRepository or;
	@Autowired
	TorneioRepository tr;

	@RequestMapping("/cadastrar")
	public ModelAndView formCadastroAtleta(@PathVariable("id_organizador") long id_organizador) throws ParseException {
		ModelAndView mv = new ModelAndView("formAtleta");
		Organizador organizador = or.findById(id_organizador);
		mv.addObject("organizador", organizador);

		Iterable<Atleta> atletas = ar.findByClube(organizador.getClube());
		List<Atleta> atletaView = new ArrayList<Atleta>();

		for (Atleta atleta : atletas) {
			if(atleta.isAtivo()) {
			atleta.setDataNascimento(Convert.calculaIdade("yyyy-MM-dd", atleta.getDataNascimento())); // setData_inicio(Convert.convertCalendarToString("dd/MM/yyyy",
																										// cal));
			atletaView.add(atleta);}
		}
		mv.addObject("atletas", atletaView);
		return mv;
	}

	@PostMapping("/cadastrar")
	public String saveAtleta(@PathVariable("id_organizador") long id_organizador, @Valid Atleta atleta,
			BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos");
			return "redirect:/{id_organizador}/atleta/cadastrar";
		}

		// BUSCAR CPF NO BANCO, SE REPETIDO
		List<Atleta> atletas = ar.findAll();
		for (Atleta atletaCPF : atletas) {
			if (atletaCPF.getCpf().intern() == atleta.getCpf().intern()) {
				attributes.addFlashAttribute("mensagem", "CPF já cadastrado");
				return "redirect:/{id_organizador}/atleta/cadastrar";
			}
		}

		// saveAtletaService(atleta, id_organizador);
		Organizador organizador = or.findById(id_organizador);
		Atleta atletaSave = new Atleta();
		atletaSave.setDataNascimento((atleta.getDataNascimento()));
		atletaSave.setNome(atleta.getNome());
		atletaSave.setCpf(atleta.getCpf());
		atletaSave.setClube(organizador.getClube());
		ar.save(atletaSave);

		return "redirect:/{id_organizador}/atleta/cadastrar";
	}

	@RequestMapping("/deletar") //excluir atleta cadastrado em um torneio nao ira tirar ele do torneio, caso ele saia do torneio nao podera ser incluido de novo
	public String deleteAtleta(@PathVariable("id_organizador") long id_organizador, long codigoAtleta) {
		//deleteAtletaService(codigoAtleta);
		ar.findById(codigoAtleta).setAtivo(false);
		ar.save(ar.findById(codigoAtleta));
		return "redirect:/{id_organizador}/atleta/cadastrar";
	}
	@ResponseBody //HIATUS
	@DeleteMapping("/deletar/{id_atleta}")
	private void deleteAtletaService(@PathVariable("id_atleta") long id_atleta) {
		Atleta atleta = ar.findById(id_atleta);
		List<Torneio> torneiosConcorridos = atleta.getTorneiosConcorridos();
		List<Torneio> torneiosVencidos = atleta.getTorneiosVencidos();
		for (Torneio torneio : torneiosConcorridos) {
			List<Atleta> atletas = torneio.getAtletasParticipantes();
			atletas.remove(atleta);
			tr.save(torneio);
		}
		for (Torneio torneio : torneiosVencidos) {
			torneio.setAtletaVencedor(null);
			tr.save(torneio);
		}
		atleta.setTorneiosConcorridos(null);
		atleta.setTorneiosVencidos(null);
		ar.delete(atleta);
	}

	@RequestMapping("/editar/{id_atleta}")
	public ModelAndView formEditarAtleta(@PathVariable("id_organizador") long id_organizador,
			@PathVariable("id_atleta") long id_atleta) {
		ModelAndView mv = new ModelAndView("Atleta");

		Organizador organizador = or.findById(id_organizador);
		Atleta atleta = ar.findById(id_atleta);
		List<Torneio> torneiosConcorridos = atleta.getTorneiosConcorridos();
		List<Torneio> torneiosVencidos = atleta.getTorneiosVencidos();

		mv.addObject("organizador", organizador);
		mv.addObject("torneiosConcorridos", torneiosConcorridos);
		mv.addObject("torneiosVencidos", torneiosVencidos);
		mv.addObject("atleta", atleta);
		return mv;
	}

	@PostMapping("/editar/{id_atleta}")
	public String updateAtleta(@PathVariable("id_organizador") long id_organizador,
			@PathVariable("id_atleta") long id_atleta, @Valid Atleta atleta, BindingResult result,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos");
			// System.out.println("O organizador de id " + id_organizador +" falhou em
			// editar o atleta de id " + atleta.getId());
			return "redirect:/{id_organizador}/atleta/editar/{id_atleta}";
		}
		// BUSCAR CPF NO BANCO, SE REPETIDO, e ele nao for da entidade que eu to
		// querendo atualizar
		List<Atleta> atletas = ar.findAll();
		atletas.remove(ar.findById(id_atleta));
		// atletas.remove(atleta);
		for (Atleta atletaCPF : atletas) {
			if (atletaCPF.getCpf().intern() == atleta.getCpf().intern()) {
				attributes.addFlashAttribute("mensagem", "CPF já cadastrado");
				return "redirect:/{id_organizador}/atleta/editar/{id_atleta}";
			}
		}
		// updateAtletaService(id_atleta,atleta);
		Atleta atletaUpdated = ar.findById(id_atleta);
		atletaUpdated.setNome(atleta.getNome());
		atletaUpdated.setDataNascimento(atleta.getDataNascimento());
		atletaUpdated.setCpf(atleta.getCpf());
		ar.save(atletaUpdated);
		return "redirect:/{id_organizador}/atleta/cadastrar";
	}
}
