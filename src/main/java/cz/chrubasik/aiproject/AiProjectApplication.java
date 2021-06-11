package cz.chrubasik.aiproject;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cz.chrubasik.aiproject.fuzzylogic.FuzzyLinguisticVariable;
import cz.chrubasik.aiproject.fuzzylogic.MamdaniInferenceMechanism;
import cz.chrubasik.aiproject.fuzzylogic.Rule;
import cz.chrubasik.aiproject.fuzzylogic.Rule.OperatorType;
import cz.chrubasik.aiproject.fuzzysets.FuzzySetRealsLinearContinuous;

@SpringBootApplication
public class AiProjectApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AiProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		MamdaniInferenceMechanism mamdaniInferenceMechanism = new MamdaniInferenceMechanism();
		mamdaniInferenceMechanism
		.addLinguisticVariable(
				FuzzyLinguisticVariable.builder()
				.name("otacky_vetraku")
				.addLinguisticValue("nizke_otacky_vetraku", FuzzySetRealsLinearContinuous.ofLeftCorner(0D, 1000D, 1500D))
				.addLinguisticValue("stredni_otacky_vetraku", FuzzySetRealsLinearContinuous.ofSymmetricTrapezoidalFuzzyNumber(1700D, 2500D, 400D))
				.addLinguisticValue("vysoke_otacky_vetraku", FuzzySetRealsLinearContinuous.ofRightCorner(2700D, 4000D, 100000D))
				.build()
				)
		.addLinguisticVariable(
				FuzzyLinguisticVariable.builder()
				.name("teplota_procesoru")
				.addLinguisticValue("nizka_teplota_procesoru", FuzzySetRealsLinearContinuous.ofLeftCorner(0D, 25D, 35D))
				.addLinguisticValue("zvysena_teplota_procesoru", FuzzySetRealsLinearContinuous.ofSymmetricTrapezoidalFuzzyNumber(37D, 60D, 5D))
				.addLinguisticValue("vysoka_teplota_procesoru", FuzzySetRealsLinearContinuous.ofSymmetricTrapezoidalFuzzyNumber(68D, 80D, 5D))
				.addLinguisticValue("prehrati_procesoru", FuzzySetRealsLinearContinuous.ofRightCorner(83D, 90D, 200D))
				.build()
				)
		.addLinguisticVariable(
				FuzzyLinguisticVariable.builder()
				.name("vytizeni_procesoru")
				.addLinguisticValue("nizke_vytizeni_procesoru", FuzzySetRealsLinearContinuous.ofLeftCorner(0D, 25D, 50D))
				.addLinguisticValue("stredni_vytizeni_procesoru", FuzzySetRealsLinearContinuous.ofRegularTriangularFuzzyNumber(30D, 50D, 70D))
				.addLinguisticValue("vysoke_vytizeni_procesoru", FuzzySetRealsLinearContinuous.ofRightCorner(50D, 75D, 100D))
				.build()
				)
		.addLinguisticVariable(
				FuzzyLinguisticVariable.builder()
				.name("rychlost_stahovani")
				.addLinguisticValue("velmi_pomala_rychlost_stahovani", FuzzySetRealsLinearContinuous.ofLeftCorner(0D, 2D, 3.5D))
				.addLinguisticValue("pomala_rychlost_stahovani", FuzzySetRealsLinearContinuous.ofRegularTrapezoidalFuzzyNumber(3.2D, 4D, 6D, 6.5D))
				.addLinguisticValue("normalni_rychlost_stahovani", FuzzySetRealsLinearContinuous.ofRegularTrapezoidalFuzzyNumber(6.3D, 7D, 15D, 15.5D))
				.addLinguisticValue("vysoka_rychlost_stahovani", FuzzySetRealsLinearContinuous.ofRegularTrapezoidalFuzzyNumber(15.2D, 17D, 50D, 60D))
				.addLinguisticValue("velmi_vysoka_rychlost_stahovani", FuzzySetRealsLinearContinuous.ofRightCorner(55D, 65D, 1000D))
				.build()
				)
		.addLinguisticVariable(
				FuzzyLinguisticVariable.builder()
				.name("kvalita_pripojeni")
				.addLinguisticValue("spatna_kvalita_pripojeni", FuzzySetRealsLinearContinuous.ofLeftCorner(0D, 25D, 50D))
				.addLinguisticValue("normalni_kvalita_pripojeni", FuzzySetRealsLinearContinuous.ofRegularTriangularFuzzyNumber(30D, 50D, 70D))
				.addLinguisticValue("dobra_kvalita_pripojeni", FuzzySetRealsLinearContinuous.ofRightCorner(50D, 75D, 100D))
				.build()
				)
		.addLinguisticVariable(
				FuzzyLinguisticVariable.builder()
				.name("chlazeni")
				.addLinguisticValue("nedostacujici_chlazeni", FuzzySetRealsLinearContinuous.ofLeftCorner(0D, 25D, 50D))
				.addLinguisticValue("dostacujici_chlazeni", FuzzySetRealsLinearContinuous.ofRegularTriangularFuzzyNumber(30D, 50D, 70D))
				.addLinguisticValue("dobre_chlazeni", FuzzySetRealsLinearContinuous.ofRightCorner(50D, 75D, 100D))
				.build()
				)
		.addLinguisticVariable(
				FuzzyLinguisticVariable.builder()
				.name("procesu_spusteno")
				.addLinguisticValue("male_mnozstvi_procesu_spusteno", FuzzySetRealsLinearContinuous.ofLeftCorner(0D, 25D, 50D))
				.addLinguisticValue("stredni_mnozstvi_procesu_spusteno", FuzzySetRealsLinearContinuous.ofRegularTriangularFuzzyNumber(30D, 50D, 70D))
				.addLinguisticValue("velke_mnozstvi_procesu_spusteno", FuzzySetRealsLinearContinuous.ofRightCorner(50D, 75D, 100D))
				.build()
				)
		.addLinguisticVariable(
				FuzzyLinguisticVariable.builder()
				.name("volna_pamet")
				.addLinguisticValue("velmi_malo_volne_pameti", FuzzySetRealsLinearContinuous.ofLeftCorner(0D, 0.5D, 0.7D))
				.addLinguisticValue("malo_volne_pameti", FuzzySetRealsLinearContinuous.ofRegularTriangularFuzzyNumber(0.6D, 1.2D, 2D))
				.addLinguisticValue("dostatek_volne_pameti", FuzzySetRealsLinearContinuous.ofRegularTrapezoidalFuzzyNumber(1.8D, 3D, 4.5D, 5D))
				.addLinguisticValue("hodne_volne_pameti", FuzzySetRealsLinearContinuous.ofRightCorner(4.8D, 6D, 100D))
				.build()
				)
		.addRule(Rule.builder()
				.antecedent("teplota_procesoru is vysoka_teplota_procesoru")
				.antecedent("teplota_procesoru is prehrati_procesoru")
				.consequent("chlazeni is nedostacujici_chlazeni")
				.operatorType(OperatorType.OR)
				.build()
				)
		.addRule(Rule.builder()
				.antecedent("volna_pamet is velmi_malo_volne_pameti")
				.antecedent("volna_pamet is malo_volne_pameti")
				.antecedent("vytizeni_procesoru is vysoke_vytizeni_procesoru")
				.consequent("procesu_spusteno is velke_mnozstvi_procesu_spusteno")
				.operatorType(OperatorType.OR)
				.build()
				)
		.addRule(Rule.builder()
				.antecedent("rychlost_stahovani is velmi_pomala_rychlost_stahovani")
				.antecedent("rychlost_stahovani is pomala_rychlost_stahovani")
				.consequent("kvalita_pripojeni is spatna_kvalita_pripojeni")
				.operatorType(OperatorType.OR)
				.build())
		.addRule(Rule.builder()
				.antecedent("vytizeni_procesoru is nizke_vytizeni_procesoru")
				.antecedent("teplota_procesoru is vysoka_teplota_procesoru")
				.consequent("chlazeni is nedostacujici_chlazeni")
				.operatorType(OperatorType.AND)
				.build()
				)
		.addRule(Rule.builder()
				.antecedent("vytizeni_procesoru is nizke_vytizeni_procesoru")
				.antecedent("teplota_procesoru is prehrati_procesoru")
				.consequent("chlazeni is nedostacujici_chlazeni")
				.operatorType(OperatorType.AND)
				.build()
				)
		.addMeasurement("teplota_procesoru", 90D)
		.addMeasurement("volna_pamet", 3D)
		.addMeasurement("vytizeni_procesoru", 20D)
		.addMeasurement("rychlost_stahovani", 5D);
		
		mamdaniInferenceMechanism.runInference();
		
		System.out.println(mamdaniInferenceMechanism.resultsInterpretation());
		
		
	}
	
	
}
