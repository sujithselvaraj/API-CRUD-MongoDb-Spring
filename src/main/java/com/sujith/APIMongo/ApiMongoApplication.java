package com.sujith.APIMongo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@SpringBootApplication
public class ApiMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiMongoApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(StudentRepository repository, MongoTemplate mongoTemplate)
	{
		return args -> {
			Address address=new Address("India","Erode","638152");
			String email="abi@gmail.com";
			Student student=new Student("abi","D",email,Gender.MALE,address, List.of("Computer Science"), BigDecimal.TEN, LocalDateTime.now());

			//UsingMongoTemplateAndQuery(repository, mongoTemplate, email, student);
			repository.findStudentByEmail(email)
					.ifPresentOrElse(s->{
						System.out.println(student +" already exists");
					},()->{
						System.out.println("Inserting Student "+ student);
						repository.insert(student);
					});
		};
	}

	private static void UsingMongoTemplateAndQuery(StudentRepository repository, MongoTemplate mongoTemplate, String email, Student student) {
		Query query=new Query();
		query.addCriteria(Criteria.where("email").is(email));
		List<Student> students= mongoTemplate.find(query,Student.class);

		if(students.size()>1)
		{
			throw new IllegalStateException("found many student with email "+ email);
		}
		if(students.isEmpty())
		{
			System.out.println("Inserting Student "+ student);
			repository.insert(student);
		}
		else {
			System.out.println(student +" already exists");
		}
	}

}
