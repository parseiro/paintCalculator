import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../parede.test-samples';

import { ParedeFormService } from './parede-form.service';

describe('Parede Form Service', () => {
  let service: ParedeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ParedeFormService);
  });

  describe('Service methods', () => {
    describe('createParedeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createParedeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            largura: expect.any(Object),
            altura: expect.any(Object),
            numPortas: expect.any(Object),
            numJanelas: expect.any(Object),
            sala: expect.any(Object),
          })
        );
      });

      it('passing IParede should create a new form with FormGroup', () => {
        const formGroup = service.createParedeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            largura: expect.any(Object),
            altura: expect.any(Object),
            numPortas: expect.any(Object),
            numJanelas: expect.any(Object),
            sala: expect.any(Object),
          })
        );
      });
    });

    describe('getParede', () => {
      it('should return NewParede for default Parede initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createParedeFormGroup(sampleWithNewData);

        const parede = service.getParede(formGroup) as any;

        expect(parede).toMatchObject(sampleWithNewData);
      });

      it('should return NewParede for empty Parede initial value', () => {
        const formGroup = service.createParedeFormGroup();

        const parede = service.getParede(formGroup) as any;

        expect(parede).toMatchObject({});
      });

      it('should return IParede', () => {
        const formGroup = service.createParedeFormGroup(sampleWithRequiredData);

        const parede = service.getParede(formGroup) as any;

        expect(parede).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IParede should not enable id FormControl', () => {
        const formGroup = service.createParedeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewParede should disable id FormControl', () => {
        const formGroup = service.createParedeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
