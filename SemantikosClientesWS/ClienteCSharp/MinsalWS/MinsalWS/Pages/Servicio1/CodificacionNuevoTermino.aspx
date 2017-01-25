<%@ Page Title="" Language="C#" MasterPageFile="~/Servicios.Master" AutoEventWireup="true" CodeBehind="CodificacionNuevoTermino.aspx.cs" Inherits="MinsalWS.Pages.CodificacionNuevoTermino" %>
<asp:Content ID="Content1" ContentPlaceHolderID="HeadContent" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">
    <div style="width:100%;">
    <table style="width:100%;">
        <tr>
            <td>
                <asp:Label ID="lblEstablecimiento" Text="Establecimiento:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtEstablecimiento" Text="" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                <asp:Label ID="lblIdConcepto" Text="ID Concepto:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtIdConcepto" Text="" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                <asp:Label ID="lblTermino" Text="Termino:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtTermino" Text="" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                <asp:Label ID="lbltipoDescripcion" Text="Tipo escripcion:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtTipoDescripcion" Text="Preferida" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                <asp:Label ID="lblEsSensibleAMayusculas" Text="Es sensible a mayusculas:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtMayusculas" Text="false" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                <asp:Label ID="lblEmail" Text="Email:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtEmail" Text="" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                <asp:Label ID="lblObservacion" Text="Observación:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtObservacion" Text="" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                <asp:Label ID="lblProfesional" Text="Profesional:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtProfesional" Text="" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                <asp:Label ID="lblProfesion" Text="Profesión:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtProfesion" Text="" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                <asp:Label ID="lblEspecialidad" Text="Especialidad:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtEspecialidad" Text="" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                
            </td>
            <td>
                <asp:Button ID="btnInvocar" Text="Invocar" runat="server" OnClick="btnInvocar_Click"></asp:Button>
            </td>
        </tr>
        </table>
        <asp:TextBox id="TextArea1" TextMode="multiline" Columns="50" Rows="5" runat="server" Width="100%"/>
    </div>
</asp:Content>
