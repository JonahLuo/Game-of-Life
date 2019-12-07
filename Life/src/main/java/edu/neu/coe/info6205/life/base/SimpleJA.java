package edu.neu.coe.info6205.life.base;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.prngine.LCG64ShiftRandom;
import io.jenetics.util.Factory;
import io.jenetics.util.RandomRegistry;

public class SimpleJA {

    public static int fitness(Genotype<BitGene> gt){
        final Game.Behavior generations = Game.run(0L, genoToPattern(gt));
        System.out.println("Ending Game of Life after " + generations + " generations");
        return (int)generations.generation;
    }

    public static String genoToPattern(Genotype<BitGene> gt){
        StringBuilder sb = new StringBuilder();
        int x = gt.getChromosome().length();
        int y = gt.length();
        for(int j = 0; j<y; j++){
            for(int i = 0; i<x; i++){
                if(gt.getChromosome(j).getGene(i).booleanValue()){
                    sb.append((x/2)-i).append(" ").append((y/2)-j).append(", ");
                }
            }
        }

        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.deleteCharAt(sb.lastIndexOf(" "));
        return sb.toString();
    }

    public static void main(String[] args){
        // 0.) Define the randomness
        Long seed  = System.currentTimeMillis();
//        Long seed = Long.parseLong("1575698857021");
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

        System.out.println(statistics);
        System.out.println(phenotype);
        System.out.println(genoToPattern(phenotype.getGenotype()));
        System.out.println(seed);
        System.out.println(statistics.getAltered());

    }

}
