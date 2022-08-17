import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ParedeFormService } from './parede-form.service';
import { ParedeService } from '../service/parede.service';
import { IParede } from '../parede.model';
import { ISala } from 'app/entities/sala/sala.model';
import { SalaService } from 'app/entities/sala/service/sala.service';

import { ParedeUpdateComponent } from './parede-update.component';

describe('Parede Management Update Component', () => {
  let comp: ParedeUpdateComponent;
  let fixture: ComponentFixture<ParedeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paredeFormService: ParedeFormService;
  let paredeService: ParedeService;
  let salaService: SalaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ParedeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ParedeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParedeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paredeFormService = TestBed.inject(ParedeFormService);
    paredeService = TestBed.inject(ParedeService);
    salaService = TestBed.inject(SalaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Sala query and add missing value', () => {
      const parede: IParede = { id: 456 };
      const sala: ISala = { id: 49334 };
      parede.sala = sala;

      const salaCollection: ISala[] = [{ id: 51633 }];
      jest.spyOn(salaService, 'query').mockReturnValue(of(new HttpResponse({ body: salaCollection })));
      const additionalSalas = [sala];
      const expectedCollection: ISala[] = [...additionalSalas, ...salaCollection];
      jest.spyOn(salaService, 'addSalaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ parede });
      comp.ngOnInit();

      expect(salaService.query).toHaveBeenCalled();
      expect(salaService.addSalaToCollectionIfMissing).toHaveBeenCalledWith(
        salaCollection,
        ...additionalSalas.map(expect.objectContaining)
      );
      expect(comp.salasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const parede: IParede = { id: 456 };
      const sala: ISala = { id: 33130 };
      parede.sala = sala;

      activatedRoute.data = of({ parede });
      comp.ngOnInit();

      expect(comp.salasSharedCollection).toContain(sala);
      expect(comp.parede).toEqual(parede);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParede>>();
      const parede = { id: 123 };
      jest.spyOn(paredeFormService, 'getParede').mockReturnValue(parede);
      jest.spyOn(paredeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parede });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parede }));
      saveSubject.complete();

      // THEN
      expect(paredeFormService.getParede).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paredeService.update).toHaveBeenCalledWith(expect.objectContaining(parede));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParede>>();
      const parede = { id: 123 };
      jest.spyOn(paredeFormService, 'getParede').mockReturnValue({ id: null });
      jest.spyOn(paredeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parede: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parede }));
      saveSubject.complete();

      // THEN
      expect(paredeFormService.getParede).toHaveBeenCalled();
      expect(paredeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParede>>();
      const parede = { id: 123 };
      jest.spyOn(paredeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parede });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paredeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSala', () => {
      it('Should forward to salaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(salaService, 'compareSala');
        comp.compareSala(entity, entity2);
        expect(salaService.compareSala).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
