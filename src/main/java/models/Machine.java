package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Machine {
    @JsonProperty("outlets")
    Outlet outlets;
    @JsonProperty("total_items_quantity")
    Beverage totalItemsQuantity;
    @JsonProperty("beverages")
    Map<String, Beverage> beverages;
}
