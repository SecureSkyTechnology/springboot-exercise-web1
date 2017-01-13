package sbe.web1.mvc.form;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import sbe.web1.mvc.validator.IdenticalField;
import sbe.web1.mvc.validator.Period;
import sbe.web1.mvc.validator.Phone;

@IdenticalField(src = "email1", dest = "email2")
@Period(from = "birthDay", to = "registeredDay")
public class UserForm {

    @NotEmpty
    @Size(max = 50, min = 3)
    String name;

    @Min(10)
    @Max(100)
    int age;

    @NotEmpty
    @Phone
    String phone1;

    @NotEmpty
    @Phone(onlyNumber = true)
    String phone2;

    @Email
    @NotEmpty
    String email1;

    @Email
    @NotEmpty
    String email2;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date birthDay;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date registeredDay;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Date getRegisteredDay() {
        return registeredDay;
    }

    public void setRegisteredDay(Date registeredDay) {
        this.registeredDay = registeredDay;
    }
}
