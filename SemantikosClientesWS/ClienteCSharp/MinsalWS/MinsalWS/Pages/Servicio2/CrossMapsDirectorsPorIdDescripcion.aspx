<%@ Page Title="" Language="C#" MasterPageFile="~/Servicios.Master" AutoEventWireup="true" CodeBehind="CrossMapsDirectorsPorIdDescripcion.aspx.cs" Inherits="MinsalWS.Pages.Servicio2.CrossMapsDirectorsPorIdDescripcion" %>
<asp:Content ID="Content1" ContentPlaceHolderID="HeadContent" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">
    <div style="width:100%;">
    <table style="width:100%;">
        <tr>
            <td>
                <asp:Label ID="lblDescripcion" Text="Descripción id:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtIdDescripcion" Text="" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                <asp:Label ID="lblConcepto" Text="Concepto id:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtIdConcepto" Text="" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                <asp:Label ID="lblEstablecimiento" Text="Establecimiento id:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtIdEstablecimiento" Text="" runat="server"></asp:TextBox>
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
