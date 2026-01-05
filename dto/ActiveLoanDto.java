package com.banking.dto;
import java.util.List;
import com.banking.entity.Emi;
import com.banking.entity.Loan;

public class ActiveLoanDto {

    private Loan loan;
    private List<Emi> emis;

    public ActiveLoanDto(Loan loan, List<Emi> emis) {
        this.loan = loan;
        this.emis = emis;
    }

    public Loan getLoan() {
        return loan;
    }

    public List<Emi> getEmis() {
        return emis;
    }
}
