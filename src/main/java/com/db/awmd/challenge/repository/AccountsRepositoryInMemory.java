package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.AccountNotExistingException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InsufficientFundsException;
import com.db.awmd.challenge.exception.SameAccountException;
import com.db.awmd.challenge.web.AccountsController;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

  private final Map<String, Account> accounts = new ConcurrentHashMap<>();

  @Override
  public void createAccount(Account account) throws DuplicateAccountIdException {
    Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
    if (previousAccount != null) {
      throw new DuplicateAccountIdException(
        "Account id " + account.getAccountId() + " already exists!");
    }
  }

  @Override
  public Account getAccount(String accountId) {
    return accounts.get(accountId);
  }

  @Override
  public void clearAccounts() {
    accounts.clear();
  }

  
 //Transfer validation checks and transfer logic to update the Map.
@Override
public synchronized void transferBetweenAccounts(String fromAccount, String toAccount, BigDecimal value)throws InsufficientFundsException,AccountNotExistingException 
{
	
	if (!fromAccount.isEmpty() && fromAccount.equalsIgnoreCase(toAccount))
	{
		log.error("From and To Accounts are same. Please choose another account number");
		throw new SameAccountException(
		        "From and To Accounts are same. Please choose another account number");
	}
	
	Account fromAccountOne =accounts.get(fromAccount); //accounts.get(1);
	Account toAccountOne =accounts.get(toAccount);
	
	if(fromAccountOne==null || toAccountOne==null)
{
	log.error("Accounts do not exist");
	throw new AccountNotExistingException(
	        "Sorry we were unable to complete the transfer as one of the accounts do not exist.");
}
else if (fromAccountOne.getBalance().compareTo(value)==-1)
{
	log.error("Insufficient funds");
	throw new InsufficientFundsException(
	        "Insufficient funds");
}



fromAccountOne.setBalance(fromAccountOne.getBalance().subtract(value));
toAccountOne.setBalance(toAccountOne.getBalance().add(value));

accounts.put(fromAccountOne.getAccountId(), fromAccountOne);
accounts.put(toAccountOne.getAccountId(), toAccountOne);

log.info("Transfer Successful");

}

}
