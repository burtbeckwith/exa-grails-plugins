package fr.exanpe.grails.datatables.builder

import fr.exanpe.grails.datatables.test.Person
import grails.test.mixin.TestFor
import spock.lang.Specification
import test.PersonDomain

@TestFor(PersonDomain)
class ColumnsBuilderSpec extends Specification {

    Person person1 = new Person(name: "Laurent", address: "10 rue de Paris", zipCode: "75001", city: "Paris", age: 35)

    def 'Filter columns with "include"'() {
        when:
        ColumnsBuilder builder = new ColumnsBuilder()
        builder.from(person1).include("name age")

        then:
        builder.build() == ["name", "age"]
    }

    def 'Column names are not case-sensitive'() {
        when:
        ColumnsBuilder builder = new ColumnsBuilder()
        builder.from(person1).include("name zipcode")

        then:
        builder.build() == ["name", "zipCode"]
    }

    def 'Filter columns with "exclude"'() {
        when:
        ColumnsBuilder builder = new ColumnsBuilder()
        builder.from(person1).exclude("zipcode")

        then:
        builder.build() == ["name", "address", "city", "age"]
    }

    def 'Add extra columns'() {
        when:
        ColumnsBuilder builder = new ColumnsBuilder()
        builder.from(person1).exclude("zipcode").add("action other")

        then:
        builder.build() == ["name", "address", "city", "age", "action", "other"]
    }

    def 'Filter columns with a GORM Domain object'() {
        given:
        PersonDomain personDomain = new PersonDomain(name: "Laurent GORM", address: "10 rue de Paris", zipCode: "75001", city: "Paris", age: 35).save()
        personDomain.validate()

        when:
        ColumnsBuilder builder = new ColumnsBuilder()
        builder.from(personDomain).include("id name zipcode")

        then:
        builder.build() == ["id", "name", "zipCode"]

        when:
        builder = new ColumnsBuilder()
        builder.from(personDomain).exclude("zipcode")

        then:
        builder.build() == ["name", "address", "city", "age", "id"]
    }

    def 'Hide columns'() {
        when:
        ColumnsBuilder builder = new ColumnsBuilder()
        builder.from(person1).hidden("age zipcode")

        then:
        builder.build() == ["name", "address", "city"]
    }

    def 'Re-order columns'() {
        when:
        ColumnsBuilder builder = new ColumnsBuilder()
        builder.from(person1).reorder("zipcode")

        then:
        builder.build() == ["zipCode", "name", "address", "city", "age"]

        when:
        builder = new ColumnsBuilder()
        builder.from(person1).reorder("zipcode age city")

        then:
        builder.build() == ["zipCode", "age", "city", "name", "address"]

        when:
        builder = new ColumnsBuilder()
        builder.from(person1).reorder("")

        then:
        builder.build() == ["name", "address", "zipCode",  "city", "age"]

        when:
        builder = new ColumnsBuilder()
        builder.from(person1).reorder(null)

        then:
        builder.build() == ["name", "address", "zipCode",  "city", "age"]
    }

    def 'Combine many filters'() {
        when:
        ColumnsBuilder builder = new ColumnsBuilder()
        builder.from(person1).exclude("age").add("action").reorder("action zipcode")

        then:
        builder.build() == ["action", "zipCode", "name", "address", "city"]

        when:
        builder = new ColumnsBuilder()
        builder.from(person1).include("name,age,zipcode").add("action").reorder("zipcode action ")

        then:
        builder.build() == ["zipCode", "action", "name", "age"]
    }
}
