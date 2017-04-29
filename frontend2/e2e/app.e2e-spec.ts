import { AdministratiePage } from './app.po';

describe('administratie App', () => {
  let page: AdministratiePage;

  beforeEach(() => {
    page = new AdministratiePage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
