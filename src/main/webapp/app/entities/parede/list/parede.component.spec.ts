import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ParedeService } from '../service/parede.service';

import { ParedeComponent } from './parede.component';

describe('Parede Management Component', () => {
  let comp: ParedeComponent;
  let fixture: ComponentFixture<ParedeComponent>;
  let service: ParedeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'parede', component: ParedeComponent }]), HttpClientTestingModule],
      declarations: [ParedeComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(ParedeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParedeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ParedeService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.paredes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to paredeService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getParedeIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getParedeIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
