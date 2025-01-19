package com.wallet.wallet.service;

//import com.wallet.dto.AccountDTO;
//import com.wallet.entity.Account;
//import com.wallet.repository.AccountRepository;
//import com.wallet.repository.UserRepository;
import com.wallet.wallet.dto.AccountDTO;
import com.wallet.wallet.entity.Account;
import com.wallet.wallet.exception.ResourceNotFoundException;
import com.wallet.wallet.repository.AccountRepository;
import com.wallet.wallet.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountDTO createAccount(AccountDTO accountDTO) {
        Account account = new Account();
        account.setName(accountDTO.getName());
        account.setType(accountDTO.getType());
        account.setBalance(accountDTO.getBalance());
        account.setUser(userRepository.findById(accountDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));

        account = accountRepository.save(account);
        return convertToDTO(account);
    }

    public List<AccountDTO> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AccountDTO convertToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setName(account.getName());
        dto.setType(account.getType());
        dto.setBalance(account.getBalance());
        dto.setUserId(account.getUser().getId());
        return dto;
    }
}
