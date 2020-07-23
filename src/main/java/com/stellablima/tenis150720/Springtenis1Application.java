package com.stellablima.tenis150720;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class Springtenis1Application {

	public static void main(String[] args) {
		SpringApplication.run(Springtenis1Application.class, args);
	}

}
//https://bcrypt-generator.com/
//https://cursos.alura.com.br/forum/topico-apos-deploy-no-heroku-o-login-nao-funciona-51933
/*
retirando @ResponseBody dos metodos dos controllers, apenas delete ficou funcional, pois nao consegui fazer os metodos service interagirem com os metodos do front
proximo passo : utilizar oa tributo ativo para inabilitar botoes e substituir exclusão

https://www.google.com/search?q=porque+retornar+json+em+front+end&oq=porque+retornar+json+em+front+end&aqs=chrome..69i57.6423j1j7&sourceid=chrome&ie=UTF-8
https://www.youtube.com/c/Devplenocom/videos?view=0&sort=da&flow=grid
https://stackoverflow.com/questions/29247523/how-to-pass-json-value-from-controller-to-html-in-thymeleaf
https://www.google.com/search?q=thymeleaf+retornar+json&oq=thymeleaf+retornar+json&aqs=chrome..69i57j0.8935j0j7&sourceid=chrome&ie=UTF-8
https://www.google.com/search?q=react+thymeleaf+juntos&sxsrf=ALeKk03G6aTZyCf2rXs7NUdL-ZoYAd5SZQ:1594925340977&ei=HKEQX4SWO9ay5OUP0-uZuAM&start=10&sa=N&ved=2ahUKEwiElJHzt9LqAhVWGbkGHdN1BjcQ8tMDegQIDhAw&biw=1366&bih=657
https://www.javaguides.net/2019/04/spring-boot-thymeleaf-crud-example-tutorial.html

colocar botoes voltar
excluir clube ?
organizador n-n
data do dia no formulario do torneio
data dinamica na inscrição de atleta
verificar organizadorfom e organizadorFORMrepository 
colocar botão de gerenciar torneio no editar torneio, sera habilitado com 16 atletas
estudar sobre addFlashAttribute para variar a cor
interface de ativar e desativar org
lista de organizadores na ficha do clube
logica pra organizador so poder editar torneio que ele criou, talvez seja necessaria logica pra conceder permissões para outros organizadores, por ex, so pode editar nao pode excluir, ou deixar uma flagizinha de organizador criador
//problemas
 deletar um atleta vencedor de um torneio
 		permitir deleção...?
 		r: se deleto atleta do torneio vencido o torneio fica sem vencedorok porem 
 		ele abre de novo, pois fica com apenas 15 atletas, criar enumerado status torneio 
 		aberto ou torneio fechado? na entidade torneio, e no atleta
 //no momento nao existe ficha de organizador porque antes tudo era atribuido ao clube
 //aproveitar Organizador.html para passar dados na transferencia e colocar dados anteriores na parte de baixo		
//futuramente essa transferencia pode ser tbm uma junção de dados de um organizador cadastrado ? acho que n

iterator https://www.guj.com.br/t/qual-e-a-vantagem-do-iterator/35565/5 Iterable<>
JSON, fazer APIs 
 */