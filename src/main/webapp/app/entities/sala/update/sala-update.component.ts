import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SalaFormService, SalaFormGroup } from './sala-form.service';
import { ISala } from '../sala.model';
import { SalaService } from '../service/sala.service';

@Component({
  selector: 'jhi-sala-update',
  templateUrl: './sala-update.component.html',
})
export class SalaUpdateComponent implements OnInit {
  isSaving = false;
  sala: ISala | null = null;

  editForm: SalaFormGroup = this.salaFormService.createSalaFormGroup();

  constructor(protected salaService: SalaService, protected salaFormService: SalaFormService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sala }) => {
      this.sala = sala;
      if (sala) {
        this.updateForm(sala);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sala = this.salaFormService.getSala(this.editForm);
    if (sala.id !== null) {
      this.subscribeToSaveResponse(this.salaService.update(sala));
    } else {
      this.subscribeToSaveResponse(this.salaService.create(sala));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISala>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(sala: ISala): void {
    this.sala = sala;
    this.salaFormService.resetForm(this.editForm, sala);
  }
}
