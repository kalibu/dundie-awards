package com.ninjaone.dundie_awards;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Component
public class AwardsCache {
    @Getter
    @Setter
    private int totalAwards;

    public void addOneAward(){
        this.totalAwards += 1;
    }
}
