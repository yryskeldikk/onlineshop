import { TestBed } from '@angular/core/testing';

import { YkShopFormService } from './yk-shop-form.service';

describe('YkShopFormService', () => {
  let service: YkShopFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(YkShopFormService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
