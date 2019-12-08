package edu.neu.coe.info6205.life.base;

import edu.neu.coe.info6205.life.library.Library;
import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.prngine.LCG64ShiftRandom;
import io.jenetics.util.Factory;
import io.jenetics.util.RandomRegistry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleJATest {

    @Test
    public void fitnessTest(){

        Genotype<BitGene> genotype = Genotype.of(BitChromosome.of(8,0.5),8);
        final Game.Behavior generations = Game.run(0L, SimpleJA.genoToPattern(genotype));
        assertEquals( SimpleJA.fitness(genotype), generations.generation);
    }

    @Test
    public void randomnessTest(){
        Long seed = Long.parseLong(Library.Seed);
        // RandomRegistry.setRandom(new LCG64ShiftRandom.ThreadLocal());
        RandomRegistry.setRandom(new LCG64ShiftRandom.ThreadSafe(seed));

        // 1.) Define the genotype (factory) suitable for the problem
        Factory<Genotype<BitGene>> gtf = Genotype.of(
                BitChromosome.of(8,0.5), 8);


        // 2.) Set up the engine - the evolution environment
        Engine<BitGene, Integer> engine = Engine
                .builder(SimpleJA::fitness,gtf)
                .populationSize(100)
                .maximizing()
                .alterers(new Mutator<>(0.5))
                .build();

        // 3.) Set up the statistic record
        final EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();

        // 4.) Do the evolution and collect the best individual
        final Phenotype<BitGene, Integer> phenotype = engine.stream()
                .limit(10)
                .peek(statistics)
                .collect(EvolutionResult.toBestPhenotype());

        String result = SimpleJA.genoToPattern(phenotype.getGenotype());
        assertTrue(result.equals(Library.Result1)
                || result.equals(Library.Result2)
                || result.equals(Library.Result3)
                || result.equals(Library.Result4));
    }


    @Test
    public void genToPatternTest(){
        Genotype<BitGene> genotype = Genotype.of(BitChromosome.of(8,0.5), 8);
        int counts  = 0;
        for(int i =0; i<8; i++){
            counts += ((BitChromosome) genotype.getChromosome(i)).bitCount();
        }
        String temp = SimpleJA.genoToPattern(genotype);
        String[] temp1 = temp.split(", ");

        assertEquals(counts, temp1.length);
    }
}
