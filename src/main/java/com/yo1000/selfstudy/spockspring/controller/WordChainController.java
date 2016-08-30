package com.yo1000.selfstudy.spockspring.controller;

import com.yo1000.selfstudy.spockspring.model.Word;
import com.yo1000.selfstudy.spockspring.service.WordChainService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

/**
 * @author yo1000
 */
@Controller
@RequestMapping("word-chain")
public class WordChainController {
    private WordChainService wordChainService;

    public WordChainController(WordChainService wordChainService) {
        this.wordChainService = wordChainService;
    }

    @GetMapping
    public String getIndex(Word word, Model model) {
        List<Word> words = getWordChainService().history();
        model.addAttribute("words", words);
        model.addAttribute("word", word);
        return "word-chain";
    }

    @PostMapping
    public String postIndex(@ModelAttribute @Valid Word word, BindingResult bindingResult, Model model) {
        List<Word> words = getWordChainService().history();

        if (getWordChainService().chainable(words.get(0).getText(), word.getText())) {
            getWordChainService().chain(word.getText());
            return "redirect:/word-chain";
        }

        bindingResult.rejectValue("text", "Controller.word.text");
        model.addAttribute("words", words);
        model.addAttribute("word", word);
        return "word-chain";
    }

    public WordChainService getWordChainService() {
        return wordChainService;
    }
}
