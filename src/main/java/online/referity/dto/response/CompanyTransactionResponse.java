package online.referity.dto.response;

import lombok.Data;
import online.referity.entity.Transaction;
import online.referity.entity.Wallet;

import java.util.List;

@Data
public class CompanyTransactionResponse {
    Wallet wallet;
    List<Transaction> transactions;
}
