package com.commandPattern.addressBook

import com.commandPattern.addressBook.commands.*
import com.commandPattern.addressBook.dataClasses.Contact
import com.commandPattern.addressBook.dataClasses.Group
import com.commandPattern.addressBook.requests.AddContactRequest
import com.commandPattern.addressBook.requests.AddGroupRequest
import com.commandPattern.addressBook.requests.EditContactRequest
import com.commandPattern.addressBook.requests.EditGroupRequest
import com.commandPattern.addressBook.storages.Storage
import com.commandPattern.addressBook.storages.Storage.Contacts.contactId
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*


fun main(args: Array<String>) {

    val url = "jdbc:mysql://localhost:3306/addressbook_db"
    val driver = "com.mysql.cj.jdbc.Driver"
    val username = "hamza"
    val password = "password"
    Database.connect(url, driver, username, password)

    val obj = AddressBook()

    val hamza = Contact(
        contactId = UUID.randomUUID(),
        firstName = "Hamza",
        lastName = "Malik",
        emails = mutableMapOf("work" to "work@gmail.com","home" to "home@gmail.com"),
        phoneNumbers = mutableMapOf("work" to "+91 123","home" to "+91 234"),
        addresses = mutableMapOf("HOME" to "ST","WORK" to "BRC"),
        groups = mutableListOf("Vayana","PDPU")
    )

    DatabaseFactory.getInstance().use {
        it.transaction {
            val contactId = Contacts.insert {
                it[this.contactId] = hamza.contactId
                it[firstName] = hamza.firstName
                it[lastName] = hamza.lastName
            } get Contacts.contactId

            hamza.emails.forEach { (type, email) ->
                Emails.insert {
                    it[Contacts.contactId] = contactId
                    it[this.type] = type
                    it[email] = email
                }
            }
            hamza.phoneNumbers.forEach { (type, phoneNumber) ->
                PhoneNumbers.insert {
                    it[Contacts.contactId] = contactId
                    it[this.type] = type
                    it[phoneNumber] = phoneNumber
                }
            }
            hamza.addresses.forEach { (type, address) ->
                Addresses.insert {
                    it[Contacts.contactId] = contactId
                    it[this.type] = type
                    it[address] = address
                }
            }
            hamza.groups.forEach { groupName ->
                val groupId = Groups.insert {
                    it[name] = groupName
                } get Groups.groupId

                ContactGroup.insert {
                    it[Contacts.contactId] = contactId
                    it[Groups.groupId] = groupId
                }
            }
        }
    }

//    transaction {
//        SchemaUtils.create(Contacts)
//        Contacts.insert {
//            it[firstName] = "Hamza"
//            it[lastName] = "Khan"
//            it[email] = "work"
//
//        }
//    }

//    val obj = AddressBook()

//    val hamza = obj.executeCommand(
//        AddContactCommand(
//            AddContactRequest(
//        "Hamza","Malik",
//        mutableMapOf("work" to "work@gmail.com","home" to "home@gmail.com"),
//        mutableMapOf("work" to "+91 123","home" to "+91 234"),
//        mutableMapOf("HOME" to "ST","WORK" to "BRC"),
//        mutableListOf("Vayana","PDPU")
//    )
//        )
//    ) as Contact
//
//    val zayn = obj.executeCommand(
//        AddContactCommand(
//        AddContactRequest("Hamza","Khan",
//            mutableMapOf("work" to "work@gmail.com","home" to "home@gmail.com"),
//            mutableMapOf("work" to "+91 123","home" to "+91 234"),
//            mutableMapOf("HOME" to "ST","WORK" to "BRC"),
//            mutableListOf("Vayana")
//        )
//        )
//    ) as Contact
//
//    val shivam = obj.executeCommand(
//        AddContactCommand(
//            AddContactRequest("Shivam","Chavda",
//        mutableMapOf("work" to "work@gmail.com","home" to "home@gmail.com"),
//        mutableMapOf("work" to "+91 123","home" to "+91 234"),
//        mutableMapOf("HOME" to "ST","WORK" to "BRC"),
//        mutableListOf("Vayana","Navrachna")
//    )
//        )
//    ) as Contact
//
//    val parth = obj.executeCommand(
//        AddContactCommand(
//            AddContactRequest("Parth","Raval",
//        mutableMapOf("work" to "parthwork@gmail.com","home" to "parthhome@gmail.com"),
//        mutableMapOf("work" to "+91 789","home" to "+91 765"),
//        mutableMapOf("HOME" to "BV","WORK" to "BRC"),
//        mutableListOf("Vayana","PDPU")
//    )
//        )
//    ) as Contact
//
//
//    println("------------------------Contacts---------------------------")
//    val allContacts = obj.executeCommand(ShowContactCommand()) as Collection<Contact>
//    for (contact in allContacts) {
//        println("Contact Id: ${contact.contactId}")
//        println("First Name: ${contact.firstName}")
//        println("Last Name: ${contact.lastName}")
//        println("Emails: ${contact.emails}")
//        println("Phone Numbers: ${contact.phoneNumbers}")
//        println("Addresses: ${contact.addresses}")
//        println("Groups: ${contact.groups}\n")
//    }
//
//
//    println("------------------------Contact Deleted--------------------------")
//    val deletedContact = obj.executeCommand(DeleteContactCommand(hamza.contactId))
//    println(deletedContact)
//    println()
//
//
//    println("------------------------Contact Edited----------------------------")
//    val editedContact = obj.executeCommand(
//        EditContactCommand(
//        zayn.contactId,
////        EditContactRequest(zayn.contactId,)
//        EditContactRequest(zayn.contactId,
//                        "Zayn",
//                        "Malik",
//                        zayn.emails,
//                        zayn.phoneNumbers,
//                        zayn.addresses,
//                        mutableListOf("One Direction","PDPU"))
//        )
//    )
//    println(editedContact)
//    println()
//
//
//    println("---------------------------Searched Contacts------------------------")
//    val searchedContacts = obj.executeCommand(SearchContactCommand("PDPU")) as List<Contact>
//    for (contact in searchedContacts) {
//        println("Contact Id: ${contact.contactId}")
//        println("First Name: ${contact.firstName}")
//        println("Last Name: ${contact.lastName}")
//        println("Emails: ${contact.emails}")
//        println("Phone Numbers: ${contact.phoneNumbers}")
//        println("Addresses: ${contact.addresses}")
//        println("Groups: ${contact.groups}\n")
//    }
//
//
//    println("--------------------------History of Commands Used----------------------------")
//    println(obj.history.joinToString("\n"))
//    println()
//
//
//    val vayana = obj.executeCommand(
//        AddGroupCommand(
//        AddGroupRequest(
//        "Vayana Interns",
//        mutableListOf(hamza,parth,zayn)
//    )
//        )
//    ) as Group
//    val people = obj.executeCommand(
//        AddGroupCommand(
//        AddGroupRequest(
//            "Peoples",
//            mutableListOf(zayn,hamza,parth,shivam)
//    )
//        )
//    ) as Group
//
//
//    println("----------------------------Groups----------------------------")
//    val allGroups = obj.executeCommand(ShowGroupsCommand()) as Collection<Group>
//    for (group in allGroups) {
//        println("Group Id: ${group.groupId}")
//        println("Group Name: ${group.groupName}")
//        println("Group Members: ${group.groupMembers}\n")
//    }
//    println()
//
//
//    println("------------------------Group Deleted--------------------------")
////    allGroups.forEach {
////        println(it.value)
////    }
//    val deletedGroup = obj.executeCommand(DeleteGroupContact(people.groupId))
//    println(deletedGroup)
//    println()
//
//
//    println("------------------------Group Edited----------------------------")
//    val editedGroup = obj.executeCommand(
//        EditGroupCommand(
//        vayana.groupId,
//        EditGroupRequest(vayana.groupId,
//            "Vayana Intern",
//            mutableListOf(shivam)
//    )
//        )
//    )
//    println(editedGroup)
//    println()
//
//
//    println("---------------------------Searched Groups------------------------")
//    val searchedGroup = obj.executeCommand(SearchGroupCommand("vayana")) as List<Group>
//    for (group in searchedGroup) {
//        println("Group Id: ${group.groupId}")
//        println("Group Name: ${group.groupName}")
//        println("Group Members: ${group.groupMembers}\n")
//    }
//    println("--------------------------History of Commands Used----------------------------")
//    println(obj.history.joinToString("\n"))
}
