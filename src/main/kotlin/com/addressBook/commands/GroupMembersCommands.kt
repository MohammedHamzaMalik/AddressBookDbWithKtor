package com.addressBook.commands


import CommandContext
import arrow.core.Either
import com.addressBook.requests.*
import com.commandPattern.addressBook.dataClasses.Contact
import com.commandPattern.addressBook.storages.GroupMemberRepo

class AddGroupMemberCommand(
    val cmd: CommandContext,
    private val req: AddGroupMemberRequest
): Command {
    override fun execute(): Either<Exception, String> {
        return GroupMemberRepo.addGroupMemberInTable(req.groupId, req.contactId)
    }
}

class DeleteGroupMemberCommand(
    val cmd: CommandContext,
    private val req: DeleteGroupMemberRequest
): Command {
    override fun execute(): Either<Exception, String> {
        return GroupMemberRepo.deleteGroupMemberInTable(req.groupId, req.contactId)
    }
}

class FetchGroupMemberCommand(
    val cmd: CommandContext,
    private val req: FetchGroupMemberRequest
): Command {
    override fun execute(): Either<Exception, Contact> {
        return GroupMemberRepo.fetchGroupMemberInTable(req.groupId, req.contactId)
    }
}

class ConnectContactwithGroupsCommand(
    val cmd: CommandContext,
    private val req: ConnectContactwithGroupsRequest
): Command {
    override fun execute(): Either<Exception, String> {
        return GroupMemberRepo.connectContactWithGroupsInTable(req.contactId, req.groupIds)
    }
}

class ConnectGroupwtihContactsCommand(
    val cmd: CommandContext,
    private val req: ConnectGroupwtihContactsRequest
): Command {
    override fun execute(): Either<Exception, String> {
        return GroupMemberRepo.connectGroupWithContactsInTable(req.groupId, req.contactIds)
    }
}

class DisplayGroupMembersCommand(
    val cmd: CommandContext
): Command {
    override fun execute(): Either<Exception, List<Contact>> {
        return GroupMemberRepo.displayAllGroupMembersInTable()
    }
}

class DisplayGroupMembersByGroupIdCommand(
    val cmd: CommandContext,
    private val req: DisplayGroupMembersByGroupIdRequest
): Command {
    override fun execute(): Either<Exception, List<Contact>> {
        return GroupMemberRepo.displayAllContactsOfGroupInTable(req.groupId)
    }
}

