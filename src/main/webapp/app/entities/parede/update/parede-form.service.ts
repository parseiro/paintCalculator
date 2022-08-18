import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IParede, NewParede } from '../parede.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IParede for edit and NewParedeFormGroupInput for create.
 */
type ParedeFormGroupInput = IParede | PartialWithRequiredKeyOf<NewParede>;

type ParedeFormDefaults = Pick<NewParede, 'id'>;

type ParedeFormGroupContent = {
  id: FormControl<IParede['id'] | NewParede['id']>;
  largura: FormControl<IParede['largura']>;
  altura: FormControl<IParede['altura']>;
  numPortas: FormControl<IParede['numPortas']>;
  numJanelas: FormControl<IParede['numJanelas']>;
  sala: FormControl<IParede['sala']>;
};

export type ParedeFormGroup = FormGroup<ParedeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ParedeFormService {
  createParedeFormGroup(parede: ParedeFormGroupInput = { id: null }): ParedeFormGroup {
    const paredeRawValue = {
      ...this.getFormDefaults(),
      ...parede,
    };
    return new FormGroup<ParedeFormGroupContent>({
      id: new FormControl(
        { value: paredeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      largura: new FormControl(paredeRawValue.largura, {
        validators: [Validators.required, Validators.min(0), Validators.max(100)],
      }),
      altura: new FormControl(paredeRawValue.altura, {
        validators: [Validators.required, Validators.min(0), Validators.max(100)],
      }),
      numPortas: new FormControl(paredeRawValue.numPortas, {
        validators: [Validators.min(0), Validators.max(20)],
      }),
      numJanelas: new FormControl(paredeRawValue.numJanelas, {
        validators: [Validators.min(0), Validators.max(100)],
      }),
      sala: new FormControl(paredeRawValue.sala),
    });
  }

  getParede(form: ParedeFormGroup): IParede | NewParede {
    return form.getRawValue() as IParede | NewParede;
  }

  resetForm(form: ParedeFormGroup, parede: ParedeFormGroupInput): void {
    const paredeRawValue = { ...this.getFormDefaults(), ...parede };
    form.reset(
      {
        ...paredeRawValue,
        id: { value: paredeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ParedeFormDefaults {
    return {
      id: null,
    };
  }
}
