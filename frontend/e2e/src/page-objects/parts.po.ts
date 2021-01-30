import {$, ElementFinder} from 'protractor';

export class Header {
  constructor(public elementFinder: ElementFinder) {
  }

  getNavigationButton(): ElementFinder {
    return this.elementFinder.$('nav > button');
  }

  getHeaderNavLinks(index: number): ElementFinder {
    return (this.elementFinder.$$('nav #navlinks ul li').get(index)).$('a');
  }

  getSearchInput() {
    return (this.elementFinder.$('#navlinks form input'));
  }

  getSearchButton() {
    return (this.elementFinder.$('#navlinks form button'));
  }
}

export class HeaderSection {
  constructor() {
  }

  getHeader(): Header {
    return new Header($('app-header'));
  }
}
