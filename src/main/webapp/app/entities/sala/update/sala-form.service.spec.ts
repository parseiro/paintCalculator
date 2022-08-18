import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sala.test-samples';

import { SalaFormService } from './sala-form.service';

describe('Sala Form Service', () => {
  let service: SalaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SalaFormService);
  });

  describe('Service methods', () => {
    describe('createSalaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSalaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
          })
        );
      });

      it('passing ISala should create a new form with FormGroup', () => {
        const formGroup = service.createSalaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
          })
        );
      });
    });

    describe('getSala', () => {
      it('should return NewSala for default Sala initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSalaFormGroup(sampleWithNewData);

        const sala = service.getSala(formGroup) as any;

        expect(sala).toMatchObject(sampleWithNewData);
      });

      it('should return NewSala for empty Sala initial value', () => {
        const formGroup = service.createSalaFormGroup();

        const sala = service.getSala(formGroup) as any;

        expect(sala).toMatchObject({});
      });

      it('should return ISala', () => {
        const formGroup = service.createSalaFormGroup(sampleWithRequiredData);

        const sala = service.getSala(formGroup) as any;

        expect(sala).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISala should not enable id FormControl', () => {
        const formGroup = service.createSalaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSala should disable id FormControl', () => {
        const formGroup = service.createSalaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
