package com.example.contacts.controllers;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
//  import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

 import com.example.contacts.models.Person;
 import com.example.contacts.repository.PersonRepository;

@Controller
public class PersonController {

    // PersonクラスのフィールドをFinalにする。
    private final PersonRepository repository;
    public PersonController(PersonRepository repository){
        this.repository = repository;
    }

    @GetMapping("/")
    // public String index(){
    // public String index(@ModelAttribute Person person){
    public String index(@ModelAttribute Person person, Model model){
    // 一覧用データの用意
    model.addAttribute("people", repository.findAll());
        return "person/index";
    }
    
    @PostMapping("/create")
    // public String create(@RequestParam String name, Model model){
    // public String create(@ModelAttribute Person person){
    // public String create(@Validated @ModelAttribute Person person, BindingResult result) {
    public String create(@Validated @ModelAttribute Person person, BindingResult result, Model model){

        // バリデーションエラーがある場合はindex.htmlを表示
        if (result.hasErrors()) {
            model.addAttribute("people", repository.findAll()); //一覧用データの用意
            return "person/index";
        }

        // createの第一引数のString nameでindexからpostされた内容を受け取る
        // attributeの第一引数のnameに第二引数の値を入れている
        // String namae = name + "さん";
        // "name"とHTMLのに表示させるためにnameを一致させる
        // model.addAttribute("name", name);
        repository.saveAndFlush(person);
        // return "person/create";
        return "redirect:/";
    }

    // 初期データの投入
    @GetMapping("/delete/{id}")
    public String remove(@PathVariable long id){
        repository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    
    public String edit(@PathVariable long id, Model model){
        model.addAttribute("person", repository.findById(id));
        return "person/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @Validated @ModelAttribute Person person, BindingResult result){
        if (result.hasErrors()){
            return "person/edit";
        }
        repository.save(person);
        return "redirect:/";
    }

    @PostConstruct
    public void dataInit(){
        Person suzuki = new Person();
        suzuki.setName("鈴木");
        suzuki.setAge(23);
        suzuki.setEmail("suzuki@email.com");
        repository.saveAndFlush(suzuki);

        Person sato = new Person();
        sato.setName("佐藤");
        sato.setAge(20);
        sato.setEmail("sato@email.com");
        repository.saveAndFlush(sato);
    }
}
