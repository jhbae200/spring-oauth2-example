package com.example.oauth2example.controller;


import com.example.oauth2example.dto.OAuthPageDTO;
import com.example.oauth2example.dto.PageErrorDTO;
import com.example.oauth2example.exception.AuthPageException;
import com.example.oauth2example.service.OAuthPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/oauth2/v1")
public class OAuthPageController {
    private final OAuthPageService oAuthPageService;

    @ExceptionHandler(value = Exception.class)
    public String authErrorPage(Exception e, Model model, HttpServletResponse response) {
        log.error("error: ", e);
        List<PageErrorDTO> pageErrors = new ArrayList<>();
        if (e instanceof BindException) {
            pageErrors = PageErrorDTO.create(((BindException) e).getAllErrors());

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {

            pageErrors.add(PageErrorDTO.create(e));

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        model.addAttribute("errors", pageErrors);
        model.addAttribute("exception", e);
        return "error";
    }

    /**
     * code 형태로 로그인 요청
     *
     * @param getAuthRequest query param (https://tools.ietf.org/html/rfc6749#section-4.1.1)
     * @param bindingResult  parameter error
     * @return login page
     * @throws BindException     parameter error
     * @throws AuthPageException process error.
     */
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public String getAuth(@ModelAttribute("authRequest") @Valid OAuthPageDTO.GetAuthRequest getAuthRequest, BindingResult bindingResult) throws Exception, AuthPageException {
        if (bindingResult.hasErrors())
            throw new BindException(bindingResult);
        oAuthPageService.checkRedirectUri(getAuthRequest);
        return "oauth2/login";
    }

    /**
     * username, password 받아서 로그인 확인 후 code 담아서 redirect.
     * 유저 정보 틀리면 login page 로 이동.
     *
     * @param postAuthRequest query param (https://tools.ietf.org/html/rfc6749#section-4.1.1)
     * @param bindingResult   parameter error
     * @return redirect url.
     */
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public String postAuth(@ModelAttribute("authRequest") @Valid OAuthPageDTO.PostAuthRequest postAuthRequest, BindingResult bindingResult) throws Exception {
        try {
            if (!bindingResult.hasErrors()) {
                return "redirect:" + oAuthPageService.postAuth(postAuthRequest, bindingResult);
            }
        } catch (BindException e) {
            log.warn("postAuth BindException: ", e);
        } catch (Exception e) {
            log.error("postAuth Error: ", e);
            throw e;
        }
        return "oauth2/login";
    }
}
