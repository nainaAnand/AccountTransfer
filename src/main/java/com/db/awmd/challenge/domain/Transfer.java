	package com.db.awmd.challenge.domain;

	import com.fasterxml.jackson.annotation.JsonCreator;
	import com.fasterxml.jackson.annotation.JsonProperty;
	import java.math.BigDecimal;
	import javax.validation.constraints.Min;
	import javax.validation.constraints.NotNull;
	import lombok.Data;
import lombok.Getter;

import org.hibernate.validator.constraints.NotBlank;
	import org.hibernate.validator.constraints.NotEmpty;

@Data
public class Transfer {
	  @NotNull
	  @NotEmpty
	  private final String fromAccount;
	  
	  @NotNull
	  @NotEmpty
	  private final String toAccount;

	  @NotNull
	  @Min(value = 0, message = "Initial value must be positive.")
	  private BigDecimal value;

	  public Transfer(String fromAccount,String toAccount) {
	    this.fromAccount = fromAccount;
	    this.toAccount = toAccount;
	  }

	  @JsonCreator
	  public Transfer(@JsonProperty("fromAccount") String fromAccount,
			  @JsonProperty("toAccount") String toAccount, 
			  @JsonProperty("value") BigDecimal value)
			  {
		  this.fromAccount = fromAccount;
		    this.toAccount = toAccount;
		    this.value = value;


	  }
}
