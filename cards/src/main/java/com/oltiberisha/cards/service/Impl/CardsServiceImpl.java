package com.oltiberisha.cards.service.Impl;

import com.oltiberisha.cards.constans.CardsConstans;
import com.oltiberisha.cards.dto.CardsDto;
import com.oltiberisha.cards.entity.Cards;
import com.oltiberisha.cards.exception.CardAlreadyExistsException;
import com.oltiberisha.cards.exception.ResourceNotFoundException;
import com.oltiberisha.cards.mapper.CardsMapper;
import com.oltiberisha.cards.repository.CardsRepository;
import com.oltiberisha.cards.service.ICardsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class CardsServiceImpl implements ICardsService {

    private final CardsRepository cardsRepository;

    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> optionalCards= cardsRepository.findByMobileNumber(mobileNumber);
        if(optionalCards.isPresent()){
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber "+mobileNumber);
        }
        cardsRepository.save(createNewCard(mobileNumber));
    }

    private Cards createNewCard(String mobileNumber) {
        Cards newCard=new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);;
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCreatedBy("Admin");
        newCard.setCreatedAt(LocalDateTime.now());
        newCard.setCardType(CardsConstans.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstans.NEW_CARD_LIMIT);
        newCard.setAvailableAmount(CardsConstans.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        return newCard;
    }
    @Override
    public CardsDto fetchCard(String mobileNumber) {
        Cards cards=cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        return CardsMapper.mapToCardsDto(cards, new CardsDto());
    }

    @Override
    public boolean updateCard(CardsDto cardsDto) {
        Cards cards = cardsRepository.findByCardNumber(cardsDto.getCardNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Card", "CardNumber", cardsDto.getCardNumber()));
        CardsMapper.mapToCards(cardsDto, cards);
        cardsRepository.save(cards);
        return  true;
    }


    @Override
    public boolean deleteCard(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        cardsRepository.deleteById(cards.getCardId());
        return true;
    }
}
