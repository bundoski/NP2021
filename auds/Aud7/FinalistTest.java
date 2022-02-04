package auds.Aud7;

import java.util.*;
import java.util.stream.Collectors;

class InvalidArgumentException extends Exception{

    public InvalidArgumentException(String msg){
        super(msg);
    }
}

class RandomPicker{

    int finalists;

    static Random RANDOM = new Random();

    public RandomPicker(int finalists){

        this.finalists = finalists;
    }
    public Collection<Integer> pick(int prizes) throws InvalidArgumentException {
        if(prizes<=0){
            throw new InvalidArgumentException("Prizes must be positive!");
        }
        if(prizes>finalists){
            throw new InvalidArgumentException(String.format("Cannot divide %d prizes to %d finalists", prizes, finalists));
        }
/*
        List<Integer> result = new ArrayList<>();
        while(result.size()!=prizes){

            int pick = RANDOM.nextInt(finalists)+1;
            if(!result.contains(pick))
                result.add(pick);
        }
        return result;
        */
    // second way, with streams.
        return RANDOM.ints(5*prizes, 1, finalists+1)
                .distinct()
                .limit(prizes)
                .boxed()
                .collect(Collectors.toList());

    }
}

public class FinalistTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int finalists = sc.nextInt();
        int prizes = sc.nextInt();

        RandomPicker fp = new RandomPicker(finalists);
        try {
            System.out.println(fp.pick(prizes));
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
    }
}
