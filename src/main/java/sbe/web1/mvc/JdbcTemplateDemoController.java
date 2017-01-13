package sbe.web1.mvc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sbe.web1.mvc.form.TinyItemForm;

@Controller
@RequestMapping("/jdbc")
public class JdbcTemplateDemoController {
    static final Logger LOG = LoggerFactory.getLogger(JdbcTemplateDemoController.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @ModelAttribute("tinyItems")
    List<TinyItem> getTinyItems() {
        return this.jdbcTemplate.query("select id, name, price, created_at, updated_at from tinyitems",
                new RowMapper<TinyItem>() {
                    @Override
                    public TinyItem mapRow(ResultSet rs, int rowNum) throws SQLException {
                        TinyItem ti = new TinyItem();
                        ti.id = rs.getLong("id");
                        ti.name = rs.getString("name");
                        ti.price = rs.getInt("price");
                        ti.createdAt = rs.getDate("created_at");
                        ti.updatedAt = rs.getDate("updated_at");
                        return ti;
                    }
                });
    }

    @RequestMapping("/")
    public String index(@ModelAttribute("tinyItemForm") TinyItemForm tinyItemForm, BindingResult result, Model m) {
        return "jdbc-demo/index";
    }

    @PostMapping("/")
    public String add(@ModelAttribute("tinyItemForm") @Validated TinyItemForm tinyItemForm, BindingResult result,
            Model m) {
        for (FieldError fe : result.getFieldErrors()) {
            LOG.info("/jdbc-demo : field error[] = {}", fe);
        }
        if (result.hasErrors()) {
            return "jdbc-demo/index";
        }
        this.jdbcTemplate.update("insert into tinyitems(name, price) values (?, ?)", tinyItemForm.getName(),
                tinyItemForm.getPrice());
        return "redirect:/jdbc/";
    }
}

class TinyItem {
    long id;
    String name;
    int price;
    Date createdAt;
    Date updatedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}