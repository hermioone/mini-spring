package org.hermione.minis.web.test.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActionImpl implements IAction {
    @Override
    public String doAction() {
        log.info("ActionImpl#doAction()");
        return "0";
    }
}
