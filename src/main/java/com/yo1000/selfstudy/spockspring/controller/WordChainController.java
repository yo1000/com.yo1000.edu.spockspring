package com.yo1000.selfstudy.spockspring.controller;

import com.yo1000.selfstudy.spockspring.service.WordChainService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String getIndex(Model model) {
        model.addAttribute("words", getWordChainService().history());
        return "word-chain";
    }

    @PostMapping
    public String postIndex(@RequestParam String word, Model model) {
        getWordChainService().chain(word);
        return "redirect:/work-chain";
    }

    public WordChainService getWordChainService() {
        return wordChainService;
    }
}
