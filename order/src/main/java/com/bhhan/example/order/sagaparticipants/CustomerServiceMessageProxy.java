package com.bhhan.example.order.sagaparticipants;

import com.bhhan.example.customer.api.channel.CustomerChannel;
import com.bhhan.example.customer.api.commands.ReserveCreditCommand;
import com.bhhan.example.customer.api.replies.CompletedPayment;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;

/**
 * Created by hbh5274@gmail.com on 2020-11-22
 * Github : http://github.com/bhhan5274
 */

public class CustomerServiceMessageProxy {
    public static final CommandEndpoint<ReserveCreditCommand> reserveCredit = CommandEndpointBuilder
            .forCommand(ReserveCreditCommand.class)
            .withChannel(CustomerChannel.COMMAND_CHANNEL)
            .withReply(CompletedPayment.class)
            .build();
}
