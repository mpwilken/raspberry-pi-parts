import {$, browser, ElementFinder} from 'protractor';

class PartCreateForm {
  constructor(public elementFinder: ElementFinder) {
  }

  getNameInput(): ElementFinder {
    return this.elementFinder.$('app-part-create input[name=name]');
  }

  getCostInput() {
    return this.elementFinder.$('app-part-create input[name=cost]');
  }

  getQuantityInput() {
    return this.elementFinder.$('app-part-create input[name=quantity]');
  }

  getDescriptionInput() {
    return this.elementFinder.$('app-part-create input[name=description]');
  }

  getUrlInput() {
    return this.elementFinder.$('app-part-create input[name=url]');
  }

  getOrderDateInput() {
    return this.elementFinder.$('app-part-create input[name=orderDate]');
  }

  getOrderIdInput() {
    return this.elementFinder.$('app-part-create input[name=orderId]');
  }

  getSubmitButton() {
    return this.elementFinder.$('.button__save');
  }
}

export class PartCreatePage {
  navigateTo(destination = '/') {
    return browser.get(destination);
  }

  getPartCreateForm() {
    return new PartCreateForm($('app-part-create'));
  }
}
