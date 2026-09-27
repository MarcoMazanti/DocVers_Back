package tg.DocVers;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DocVersApplication {

	public static void main(String[] args) {
		Dotenv dotenv = carregarDotenv();

		SpringApplication.run(DocVersApplication.class, args);
	}

	private static Dotenv carregarDotenv() {
		try {
			Dotenv dotenv = Dotenv.load();
			dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
			System.out.println("Variáveis do .env carregadas com sucesso na JVM.");
			return dotenv;
		} catch (Exception e) {
			System.out.println("Arquivo .env não encontrado. Utilizando variáveis de ambiente do sistema ou application.properties.");
			return null;
		}
	}
}
