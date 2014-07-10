package com.sirishrenukumar.mfa;

import java.io.IOException;
import java.util.Arrays;

import javax.inject.Inject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.sirishrenukumar.mfa.entity.MutualFund;
import com.sirishrenukumar.mfa.entity.Person;
import com.sirishrenukumar.mfa.entity.managers.MutualFundManager;
import com.sirishrenukumar.mfa.entity.managers.PersonDaoImpl;
import com.sirishrenukumar.mfa.parser.MutualFundSnapshotSummaryParser;
import com.sirishrenukumar.mfa.parser.MutualFundPortfolioParser;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class MutualFundAnalyzer {

	public static void main(String[] args) throws IOException {
		ApplicationContext ctx = SpringApplication.run(
				MutualFundAnalyzer.class, args);


		try {
			MutualFundSnapshotSummaryParser mutualFundSnapshotSummaryParser = ctx.getBean(MutualFundSnapshotSummaryParser.class);
			MutualFundPortfolioParser mutualFundPortfolioParser = ctx.getBean(MutualFundPortfolioParser.class); 
			MutualFundManager mutualFundManager = ctx.getBean(MutualFundManager.class);
			
			mutualFundSnapshotSummaryParser.parse();
			mutualFundPortfolioParser.parse();
			
//			PersonDaoImpl personDaoImpl = ctx.getBean(PersonDaoImpl.class);
//			personDaoImpl.save(new Person("sirish", "renukumar"));
//			personDaoImpl.save(new Person("sirish", "renukumar"));
//			System.out.println(personDaoImpl.getAll());
			
			for(MutualFund mutualFund : mutualFundManager.getMutualFunds()) {
				System.out.println(mutualFund);
			}
			

		} finally {
			if (ctx != null) {
				((AnnotationConfigApplicationContext) ctx).close();
			}
		}
	}
}
