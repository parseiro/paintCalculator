import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ParedeDetailComponent } from './parede-detail.component';

describe('Parede Management Detail Component', () => {
  let comp: ParedeDetailComponent;
  let fixture: ComponentFixture<ParedeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ParedeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ parede: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ParedeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ParedeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load parede on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.parede).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
