package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Beverage {
    @JsonProperty("hot_water")
    Long hotWater;

    @JsonProperty("hot_milk")
    Long hotMilk;

    @JsonProperty("green_mixture")
    Long greenMixture;

    @JsonProperty("ginger_syrup")
    Long gingerSyrup;

    @JsonProperty("sugar_syrup")
    Long sugarSyrup;

    @JsonProperty("tea_leaves_syrup")
    Long teaLeavesSyrup;

    @JsonProperty("elaichi_syrup")
    Long elaichiSyrup;

}
