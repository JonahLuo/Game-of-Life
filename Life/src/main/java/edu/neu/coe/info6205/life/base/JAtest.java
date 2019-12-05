package edu.neu.coe.info6205.life.base;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;

public class JAtest {

    public static int fitness(Genotype<BitGene> gt){
        int value = 0;
//        byte[] x = ((BitChromosome) gt.getChromosome(0)).toByteArray();
//        byte[] y = ((BitChromosome) gt.getChromosome(1)).toByteArray();
//        for(int i = 0; i<x.length; i++){
//            byte tempX = x[i];
//            byte tempY = y[i];
//            int mask = 1;
//            for(int j = 0; j< 8; j++){
//                if((mask & tempX & tempY) != 0) value++;
//                mask = mask << 1;
//            }
//        }
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

        sb.deleteCharAt(sb.lastIndexOf(", "));
        return sb.toString();
    }

    public static void main(String[] args){
        // 1.) Define the genotype (factory) suitable for the problem
        Factory<Genotype<BitGene>> gtf = Genotype.of(
                BitChromosome.of(8,0.5), 8);

        Engine<BitGene, Integer> engine = Engine
                .builder(JAtest::fitness,gtf)
                .populationSize(100)
                .alterers(new Mutator<>(0.5))
                .build();

        final Phenotype<BitGene, Integer> phenotype = engine.stream()
                .limit(10)
                .collect(EvolutionResult.toBestPhenotype());

//
        System.out.println(phenotype);
        System.out.println(genoToPattern(phenotype.getGenotype()));

//        Genotype<BitGene> genotype = Genotype.of(BitChromosome.of(8,0.5),8);
//        BitChromosome bt = (BitChromosome) genotype.getChromosome(0);
//        byte[] result = bt.toByteArray();
//
//        System.out.println(genotype);
//        System.out.print(genotype.getChromosome(0) + " ");
//        System.out.println(genotype.getChromosome(1));
//        System.out.print(genotype.getChromosome(0).getGene(4).booleanValue() + " ");
//        System.out.println(genotype.getChromosome(1).getGene(5).booleanValue());
//        System.out.println(genoToPattern(genotype));
//        System.out.println(genotype.length());
//        System.out.println(genotype.getChromosome(40));
//        System.out.println(genotype.getChromosome(40).getGene(15).booleanValue());
//        System.out.println(result.length);
//        System.out.println(genotype.getChromosome().length());

    }

}
