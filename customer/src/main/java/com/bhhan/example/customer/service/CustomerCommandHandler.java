package com.bhhan.example.customer.service;

import com.bhhan.example.customer.api.channel.CustomerChannel;
import com.bhhan.example.customer.api.commands.ReserveCreditCommand;
import com.bhhan.example.customer.api.replies.CompletedPayment;
import com.bhhan.example.customer.api.replies.CustomerCreditLimitExceeded;
import com.bhhan.example.customer.api.replies.CustomerNotFound;
import com.bhhan.example.customer.domain.CustomerCreditLimitExceededException;
import com.bhhan.example.customer.service.exception.CustomerNotFoundException;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import lombok.RequiredArgsConstructor;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

/**
 * Created by hbh5274@gmail.com on 2020-11-22
 * Github : http://github.com/bhhan5274
 */

@RequiredArgsConstructor
public class CustomerCommandHandler {
    private final CustomerService customerService;

    public CommandHandlers commandHandler(){
        return SagaCommandHandlersBuilder
                .fromChannel(CustomerChannel.COMMAND_CHANNEL)
                .onMessage(ReserveCreditCommand.class, this::reserveCredit)
                .build();
    }

    private Message reserveCredit(CommandMessage<ReserveCreditCommand> cm){
        ReserveCreditCommand cmd = cm.getCommand();

        try {
            return withSuccess(new CompletedPayment(customerService.reserveCredit(cmd)));
        }catch(CustomerNotFoundException e){
            return withFailure(new CustomerNotFound());
        }catch(CustomerCreditLimitExceededException e){
            return withFailure(new CustomerCreditLimitExceeded());
        }
    }
}
