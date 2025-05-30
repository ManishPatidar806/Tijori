package com.financialapplication.expansesanalysis.Service;

import com.financialapplication.expansesanalysis.Exception.NotFoundException;
import com.financialapplication.expansesanalysis.Helper.EmailSender;
import com.financialapplication.expansesanalysis.Model.Entity.Category;
import com.financialapplication.expansesanalysis.Model.Entity.Money;
import com.financialapplication.expansesanalysis.Model.Entity.Sms;
import com.financialapplication.expansesanalysis.Model.Entity.User;
import com.financialapplication.expansesanalysis.Model.Enum.CategoryType;
import com.financialapplication.expansesanalysis.Model.Enum.MoneyType;
import com.financialapplication.expansesanalysis.Model.Request.SmsSavedRequest;
import com.financialapplication.expansesanalysis.Model.Request.SmsUpdateRequest;
import com.financialapplication.expansesanalysis.Model.Response.CommonResponse;
import com.financialapplication.expansesanalysis.Model.Response.SmsResponse;
import com.financialapplication.expansesanalysis.Repository.CategoryRepository;
import com.financialapplication.expansesanalysis.Repository.MoneyRepository;
import com.financialapplication.expansesanalysis.Repository.SmsRepository;
import com.financialapplication.expansesanalysis.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class SmsServiceImpl implements SmsService {

    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final MoneyRepository moneyRepository;
    private final SmsRepository smsRepository;
    private final EmailSender emailSender;
    private final CategoryRepository categoryRepository;
    private final Logger logger = Logger.getLogger(getClass().getName());



    public SmsServiceImpl(ModelMapper mapper, UserRepository userRepository, MoneyRepository moneyRepository,
                          SmsRepository smsRepository, EmailSender emailSender, CategoryRepository categoryRepository) {
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.moneyRepository = moneyRepository;
        this.smsRepository = smsRepository;
        this.emailSender = emailSender;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ResponseEntity<CommonResponse> savedSms(SmsSavedRequest smsRequest, String mobileNo)
            throws NotFoundException {
        logger.info("Saving SMS for mobile number: " + mobileNo);

        if (smsRequest.getSms().isEmpty()) {
            return new ResponseEntity<>(new CommonResponse("No new messages found.", true), HttpStatus.NO_CONTENT);
        }

        User user = fetchUserByMobile(mobileNo);
        LocalDateTime smsTime = smsRepository.findLatestDateTime();
        LocalDateTime oneMonthBeforeUserCreated = user.getCreatedAt().minusMonths(1);

        List<Sms> filteredSmsList = smsRequest.getSms().stream()
                .map(smsDto -> mapper.map(smsDto, Sms.class))
                .filter(sms -> isSmsValid(sms, smsTime, oneMonthBeforeUserCreated))
                .peek(sms -> sms.setUser(user))
                .toList();

        if (filteredSmsList.isEmpty()) {
            return new ResponseEntity<>(new CommonResponse("No new messages to save.", true), HttpStatus.NO_CONTENT);
        }

        Map<Boolean, List<Sms>> partitioned = partitionSmsByType(filteredSmsList);

        double credited = calculateTotalAmount(partitioned.get(true));
        double debited = calculateTotalAmount(partitioned.get(false));
        double expenses = calculateExpenses(filteredSmsList, user.getCreatedAt(), smsTime);

        Money money = moneyRepository.findByUserId(user.getId()).orElseGet(() -> createNewMoney(user));
        updateMoney(money,user.getName(), credited, debited, expenses,user.getEmail());

        smsRepository.saveAll(filteredSmsList);
        moneyRepository.save(money);

        return new ResponseEntity<>(new CommonResponse("SMS saved successfully.", true), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SmsResponse> getAllSms(String mobileNo) throws NotFoundException {
        User user = fetchUserByMobile(mobileNo);
        List<Sms> smsList = smsRepository.getAllSmsOfCategoryNull(mobileNo, user.getCreatedAt());

        if (smsList.isEmpty()) {
            return new ResponseEntity<>(new SmsResponse("No SMS found.", true, smsList), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(new SmsResponse("SMS found successfully.", true, smsList), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SmsResponse> getSmsByCategory(String mobileNo, CategoryType category)
            throws NotFoundException {
        User user = fetchUserByMobile(mobileNo);
        List<Sms> smsList = smsRepository.getAllSmsByCategory(mobileNo, category, user.getCreatedAt());

        if (smsList.isEmpty()) {
            return new ResponseEntity<>(new SmsResponse("No SMS found for the selected category.", true, smsList),
                    HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(new SmsResponse("SMS found successfully for the selected category.", true, smsList),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CommonResponse> updateCategory(String mobileNo, SmsUpdateRequest smsRequest)
            throws NotFoundException {
        User user = fetchUserByMobile(mobileNo);

        List<Sms> filteredSms = smsRequest.getSms().stream()
                .map(smsDto -> mapper.map(smsDto, Sms.class))
                .filter(sms -> sms.getCategory() != null)
                .peek(sms -> sms.setUser(user))
                .toList();

        Category category = categoryRepository.findCategoryByUserId(user.getId())
                .orElseGet(() -> createNewCategory(user));

        updateCategoryAmounts(category, filteredSms);

        smsRepository.saveAll(filteredSms);
        categoryRepository.save(category);

        return new ResponseEntity<>(new CommonResponse("Category updated successfully.", true), HttpStatus.OK);
    }

    private User fetchUserByMobile(String mobileNo) throws NotFoundException {
        return userRepository.findByMobile(mobileNo)
                .orElseThrow(() -> new NotFoundException("User not found with mobile number: " + mobileNo));
    }

    private boolean isSmsValid(Sms sms, LocalDateTime smsTime, LocalDateTime oneMonthBeforeUserCreated) {
        LocalDateTime smsDateTime = sms.getDateTime();
        return smsDateTime != null &&
                (smsTime == null || smsDateTime.isAfter(smsTime)) &&
                smsDateTime.isAfter(oneMonthBeforeUserCreated);
    }

    private Map<Boolean, List<Sms>> partitionSmsByType(List<Sms> smsList) {
        return smsList.stream()
                .collect(Collectors.partitioningBy(sms -> sms.getMoneyType() == MoneyType.CREDITED));
    }

    private double calculateTotalAmount(List<Sms> smsList) {
        return smsList.stream().mapToDouble(Sms::getAmount).sum();
    }

    private double calculateExpenses(List<Sms> smsList, LocalDateTime userCreatedAt, LocalDateTime smsTime) {
        return smsList.stream()
                .filter(sms -> {
                    LocalDateTime smsDateTime = sms.getDateTime();
                    return smsDateTime != null &&
                            smsDateTime.isAfter(userCreatedAt) &&
                            (smsTime == null || smsDateTime.isAfter(smsTime));
                })
                .mapToDouble(Sms::getAmount)
                .sum();
    }

    private Money createNewMoney(User user) {
        Money money = new Money();
        money.setUser(user);
        return money;
    }

    private Category createNewCategory(User user) {
        Category category = new Category();
        category.setUser(user);
        return category;
    }

    private void updateMoney(Money money,String name, double credited, double debited, double expenses, String email) {
        money.setCreditAmount(money.getCreditAmount() + credited);
        money.setDebitedAmount(money.getDebitedAmount() + debited);
        double notificationLimit = money.getMonthlyLimit()-expenses;
        double percentage = (money.getFixedLimit()*10)/100;
        if(percentage>=notificationLimit){
        emailSender.sendNotificationEmail(email,name,notificationLimit,money.getFixedLimit()    );
        }

        money.setMonthlyLimit(money.getMonthlyLimit() - expenses);
    }

    private void updateCategoryAmounts(Category category, List<Sms> filteredSms) {
        Map<CategoryType, Double> categoryAmounts = filteredSms.stream()
                .collect(Collectors.groupingBy(Sms::getCategory, Collectors.summingDouble(Sms::getAmount)));

        categoryAmounts.forEach((categoryType, amount) -> {
            switch (categoryType) {
                case GROCERIES -> category.setGroceries(category.getGroceries() + amount);
                case MEDICAL -> category.setMedical(category.getMedical() + amount);
                case DOMESTIC -> category.setDomestic(category.getDomestic() + amount);
                case SHOPPING -> category.setShopping(category.getShopping() + amount);
                case BILLS -> category.setBills(category.getBills() + amount);
                case ENTERTAINMENT -> category.setEntertainment(category.getEntertainment() + amount);
                case TRAVELLING -> category.setTravelling(category.getTravelling() + amount);
                case FUELING -> category.setFueling(category.getFueling() + amount);
                case EDUCATIONAL -> category.setEducational(category.getEducational() + amount);
                case OTHERS -> category.setOthers(category.getOthers() + amount);
            }
        });
    }
}