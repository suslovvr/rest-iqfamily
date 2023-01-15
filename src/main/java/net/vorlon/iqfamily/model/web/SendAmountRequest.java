package net.vorlon.iqfamily.model.web;

import com.sun.istack.NotNull;
import lombok.Data;

//import javax.validation.constraints.NotNull;
@Data
public class SendAmountRequest {
    @NotNull
    private String userName;
    @NotNull
    private String amount;




}